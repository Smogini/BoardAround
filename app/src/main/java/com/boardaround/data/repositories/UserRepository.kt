package com.boardaround.data.repositories

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

    suspend fun deleteUser() {
        val userUID = FirebaseAuth.getInstance().uid
        if (userUID != null) {
            userDao.deleteUser(userUID)
        }
    }

    fun setLoggedUser(user: User) {
        sessionManager.setUserLoggedIn(user)
    }

    suspend fun registerUser(newUser: User) {
        userDao.insertUser(newUser)
    }

    suspend fun createFirebaseUser(email: String, password: String): String =
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .await().user?.uid ?: "No UID"

    private suspend fun syncUserFirestoreToRoom(uid: String): User {
        val firestore = FirebaseFirestore.getInstance()

        val userSnapshot = firestore.collection("users")
            .document(uid)
            .get()
            .await()

        if (!userSnapshot.exists()) {
            throw Exception("User not found on Firestore")
        }

        val userData = userSnapshot.toObject(User::class.java)
            ?: throw Exception("Unable to convert user data")

        userDao.insertUser(userData)

        return userData
    }

    suspend fun login(username: String, password: String): User? {
        val userFromRoom = userDao.getUserByUsername(username)

        val userEmail = userFromRoom?.email.run { "$username@gmail.com" }

        val authResult = FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail, password).await()
        val userFromFirebase = authResult.user ?: return null

        val fcmToken = FirebaseMessaging.getInstance().token.await()

        firestore.collection("users")
            .document(userFromFirebase.uid)
            .update("fcmToken", fcmToken)
            .await()

        if (userFromRoom != null) {
            return userFromRoom
        }

        return syncUserFirestoreToRoom(userFromFirebase.uid)
    }

    fun logout() {
        sessionManager.logout()
        FirebaseAuth.getInstance().signOut()
    }

    fun isUserLoggedIn(): Boolean =
        sessionManager.isUserLoggedIn()

    suspend fun searchUsersByUsername(username: String): List<User> =
        userDao.retrieveUsersByUsername(username)

    fun getCurrentUser(): User? =
        sessionManager.getCurrentUser()

}
