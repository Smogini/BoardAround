package com.boardaround.data.repositories

import android.content.Context
import android.util.Log
import androidx.compose.ui.geometry.isEmpty
import com.boardaround.data.UserSessionManager
import com.boardaround.data.dao.UserDAO
import com.boardaround.data.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await


class UserRepository(
    context: Context,
    private val userDao: UserDAO,
    private val firestore: FirebaseFirestore
) {

    private val sessionManager = UserSessionManager(context)

    suspend fun deleteUser(toDelete: User) {
        userDao.deleteUser(toDelete)
    }

    fun saveUser(user: User) {
        sessionManager.setUserLoggedIn(user, true)
    }


    suspend fun registerUser(
        username: String,
        email: String,
        password: String,
        name: String,
        dob: String,
        profilePic: String
    ) {
        try {
            // 1. Crea l'utente in Firebase Authentication
            val authResult = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("Firebase user is null after registration")

            // 2. Ottieni il token FCM
            val fcmToken = FirebaseMessaging.getInstance().token.await()

            // 3. Prepara i dati utente per Firestore e Room
            val newUser = User(
                username = username,
                uid = firebaseUser.uid,
                name = name,
                email = email,
                dob = dob,
                profilePic = profilePic,  // Usa il link dell'immagine come profilePic
                fcmToken = fcmToken
            )

            // 4. Salva i dati utente in Firestore (usando l'UID come ID del documento)
            firestore.collection("users")
                .document(newUser.uid)
                .set(newUser)
                .await()
            Log.d("Registration", "Utente salvato in Firestore con UID: ${newUser.uid}")

            // 5. Salva i dati utente in Room (opzionale)
            userDao.insertUser(newUser)
            Log.d("Registration", "Utente salvato in Room: ${newUser.username}")

        } catch (e: FirebaseAuthException) {
            Log.e("Registration", "Errore Firebase Auth durante la registrazione", e)
            throw e
        } catch (e: Exception) {
            Log.e("Registration", "Errore durante la registrazione", e)
            throw e
        }
    }

    suspend fun getUserFromFirebase(username: String): User? {
        return try {
            val snapshot = firestore.collection("users")
                .document(username)
                .get()
                .await()
            if (snapshot.exists()) {
                snapshot.toObject(User::class.java)
            } else null
        } catch (e: Exception) {
            Log.e("UserRepository", "Errore recupero utente Firebase", e)
            null
        }
    }

    suspend fun syncUserToRoomIfNeeded(username: String) {
        val localUser = userDao.getUserByUsername(username)
        if (localUser == null) {
            val firebaseUser = getUserFromFirebase(username)
            firebaseUser?.let {
                userDao.insertUser(it)
                Log.d("UserRepository", "Utente sincronizzato in Room: $username")
            } ?: Log.w("UserRepository", "Utente non trovato su Firebase: $username")
        }
    }




    suspend fun login(username: String, password: String): Boolean {
        try {
            val userFromRoom = userDao.getUser(username)
            val email = userFromRoom?.email ?: run {
                // Se non trovi l'utente localmente, prova a cercarlo in Firestore
                Log.w("UserRepository", "Utente con username $username non trovato nel database locale.")

                try {
                    val userFromFirestoreQuery = firestore.collection("users")
                        .whereEqualTo("username", username)
                        .limit(1)
                        .get()
                        .await()

                    if (!userFromFirestoreQuery.isEmpty) {
                        val userFirestoreData = userFromFirestoreQuery.documents[0].data
                        userFirestoreData?.get("email") as? String
                            ?: throw Exception("Email non trovata in Firestore per username $username")
                    } else {
                        throw Exception("Utente con username $username non trovato in Firestore.")
                    }
                } catch (e: Exception) {
                    Log.e("UserRepository", "Errore durante il recupero dell'email da Firestore per username $username", e)
                    return false
                }
            }

            // Procedi con il login su Firebase
            val authResult = FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("Firebase user is null after successful authentication.")

            // Ottieni il token FCM e aggiorna Firestore
            val fcmToken = FirebaseMessaging.getInstance().token.await()
            firestore.collection("users")
                .document(firebaseUser.uid)
                .update("fcmToken", fcmToken)
                .await()

            // Recupera i dati utente da Firestore e aggiorna Room
            val userFromFirestore = firestore.collection("users")
                .document(firebaseUser.uid)
                .get()
                .await()
                .toObject(User::class.java)

            userFromFirestore?.let {
                userDao.insertUser(it)  // Inserisce o aggiorna i dati utente in Room
                Log.d("UserRepository", "Dati utente aggiornati in Room per username: ${it.username}")
            }

            return true

        } catch (e: FirebaseAuthException) {
            Log.e("UserRepository", "Errore Firebase Auth durante il login", e)
            return false
        } catch (e: Exception) {
            Log.e("UserRepository", "Errore generale durante il login", e)
            return false
        }
    }


    fun logout() {
        sessionManager.logout()
    }

    fun isUserLoggedIn(): Boolean {
        return sessionManager.isUserLoggedIn()
    }

    suspend fun searchUsersByUsername(username: String): List<User> {
        val users = userDao.retrieveUsersByUsername(username)
        return users
    }

    suspend fun saveUserDataAfterAuth(
        firebaseUser: FirebaseUser,
        username: String,
        name: String,
        dob: String,
        profilePic: String,
        fcmToken: String?
    ) {
        try {
            val userData = hashMapOf(
                "uid" to firebaseUser.uid, // Salva l'UID di Firebase Auth
                "username" to username, // Salva il username (se lo usi come identificatore secondario)
                "name" to name,
                "email" to firebaseUser.email, // Ottieni l'email da FirebaseUser
                "dob" to dob,
                "profilePic" to profilePic,
                "fcmToken" to fcmToken, // Salva il token FCM
                "createdAt" to System.currentTimeMillis()
            )

            firestore.collection("users")
                .document(firebaseUser.uid) // Usa l'UID come ID del documento
                .set(userData)
                .await()

            Log.d("UserRepository", "Dati utente salvati in Firestore per UID: ${firebaseUser.uid}")

        } catch (e: Exception) {
            Log.e("UserRepository", "Errore durante il salvataggio dei dati utente in Firestore", e)
            throw e
        }
    }

    suspend fun updateUserFcmToken(uid: String, fcmToken: String?) {
        try {
            firestore.collection("users")
                .document(uid)
                .update("fcmToken", fcmToken)
                .await()
            Log.d("UserRepository", "FCM Token aggiornato in Firestore per UID: $uid")
        } catch (e: Exception) {
            Log.e("UserRepository", "Errore aggiornamento FCM Token in Firestore", e)
            throw e
        }
    }
}
