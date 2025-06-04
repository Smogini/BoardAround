package com.boardaround.data.repositories

import com.boardaround.data.UserSessionManager
import com.boardaround.data.dao.UserDAO
import com.boardaround.data.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val sessionManager: UserSessionManager,
    private val userDao: UserDAO,
    private val firestoreDB: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {
    val currentUser: StateFlow<User?> = sessionManager.currentUser

    suspend fun deleteUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            sessionManager.logout()
            userDao.deleteUser(firebaseUser.uid)
            firebaseUser.delete()
            firestoreDB.collection("users")
                .document(firebaseUser.uid)
                .delete()
                .await()
        }
    }

    suspend fun registerUser(newUser: User) {
        userDao.insertUser(newUser)
    }

    suspend fun createFirebaseUser(email: String, password: String): FirebaseUser =
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .await().user ?: throw Exception("User not found")

    private suspend fun syncUserFirestoreToRoom(uid: String) {
        val userSnapshot = firestoreDB.collection("users")
            .document(uid)
            .get()
            .await()

        if (!userSnapshot.exists()) {
            throw Exception("User not found on Firestore")
        }

        val userData = userSnapshot.toObject(User::class.java)
            ?: throw Exception("Unable to convert user data")

        if (userDao.getUserByUID(userData.uid) == null) {
            userDao.insertUser(userData)
        }
    }

    suspend fun login(username: String, password: String) {
        val email = "$username@gmail.com"

        val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        val firebaseUser = authResult.user ?: throw Exception("User is null")

        val uid = firebaseUser.uid

        val documentSnapshot = firestoreDB.collection("users")
            .document(uid)
            .get()
            .await()

        if (!documentSnapshot.exists()) {
            throw Exception("User document not found for UID: $uid")
        }

        val user = documentSnapshot.toObject(User::class.java)
            ?: throw Exception("Error converting document to User")

        val fcmToken = FirebaseMessaging.getInstance().token.await()
        firestoreDB.collection("users")
            .document(uid)
            .update("fcmToken", fcmToken)
            .await()

        sessionManager.setUserLoggedIn(user)
        syncUserFirestoreToRoom(uid)
    }

    fun logout() {
        firebaseAuth.signOut()
        sessionManager.logout()
    }

    fun isUserLoggedIn(): Boolean =
        sessionManager.isUserLoggedIn()

    suspend fun searchUsersByUsername(username: String): List<User> {
        val fromRoom = userDao.retrieveUsersByUsername(username)

        if (fromRoom.isNotEmpty()) return fromRoom

        val fromFirestore = firestoreDB.collection("users")
            .orderBy("username")
            .startAt(username)
            .endAt("$username\uf8ff")
            .get()
            .await()

        val users = fromFirestore.documents.mapNotNull { it.toObject(User::class.java) }

        users.forEach { userDao.insertUser(it) }

        return users
    }

}
