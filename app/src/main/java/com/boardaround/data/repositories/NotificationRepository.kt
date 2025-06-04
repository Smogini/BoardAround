package com.boardaround.data.repositories

import com.boardaround.data.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NotificationRepository(private val firestore: FirebaseFirestore) {

    private val auth = FirebaseAuth.getInstance()

    fun getCurrentUser(callback: (User?) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            firestore.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { doc ->
                    val user = doc.toObject(User::class.java)
                    callback(user)
                }
                .addOnFailureListener {
                    callback(null)
                }
        } else {
            callback(null)
        }
    }

    fun hasUnread(): Boolean {
        // Da implementare
        return true
    }
}
