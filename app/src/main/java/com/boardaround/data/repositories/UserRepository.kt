package com.boardaround.data.repositories

import android.util.Log
import com.boardaround.data.UserSessionManager
import com.boardaround.data.dao.UserDAO
import com.boardaround.data.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val sessionManager: UserSessionManager,
    private val userDao: UserDAO,
    private val firestore: FirebaseFirestore
) {

    suspend fun deleteUser(toDelete: User) {
        userDao.deleteUser(toDelete)
    }

    fun setLoggedUser(user: User) {
        sessionManager.setUserLoggedIn(user)
    }

    suspend fun registerUser(newUser: User) {
        userDao.insertUser(newUser)
    }

    private suspend fun getUserFromFirebase(username: String): User? {
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

    suspend fun login(username: String, password: String): User? {
        val userFromFirebase = FirebaseAuth.getInstance().currentUser
        val userFromRoom = userDao.getUserByUsername(username)

        /* user already logged */
        if (userFromFirebase != null && userFromFirebase.displayName == username) {
            return userFromRoom
        }

        if (userFromRoom != null) {
            val authResult = FirebaseAuth.getInstance().signInWithEmailAndPassword(userFromRoom.email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("Firebase user is null after successful authentication.")
            val fcmToken = FirebaseMessaging.getInstance().token.await()
            firestore.collection("users")
                .document(firebaseUser.uid)
                .update("fcmToken", fcmToken)
                .await()
            return userFromRoom
        }
        return null
        // Recupera i dati utente da Firestore e aggiorna Room
//            val userFromFirestore = firestore.collection("users")
//                .document(firebaseUser.uid)
//                .get()
//                .await()
//                .toObject(User::class.java)
//
//            userFromFirestore?.let {
//                userDao.insertUser(it)  // Inserisce o aggiorna i dati utente in Room
//                Log.d("UserRepository", "Dati utente aggiornati in Room per username: ${it.username}")
//            }
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

    fun getCurrentUser(): User? =
        sessionManager.getCurrentUser()

}
