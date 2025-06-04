package com.boardaround.data.repositories

import com.boardaround.data.entities.Friendship
import com.boardaround.data.entities.User
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FriendshipRepository(private val firestore: FirebaseFirestore) {

    suspend fun getAcceptedFriendUsernames(currentUserUID: String): List<String> {
        val sent = firestore.collection("friends")
            .whereEqualTo("status", "accepted")
            .whereEqualTo("fromUserUID", currentUserUID)
            .get()
            .await()

        val received = firestore.collection("friends")
            .whereEqualTo("status", "accepted")
            .whereEqualTo("toUserUID", currentUserUID)
            .get()
            .await()

        return (sent.documents + received.documents).mapNotNull { doc ->
            doc.toObject(Friendship::class.java)?.let { fr ->
                if (fr.fromUserId == currentUserUID) fr.toUserId else fr.fromUserId
            }
        }.distinct()
    }

    suspend fun getUsersByUsernames(usernames: List<String>): List<User> {
        if (usernames.isEmpty()) {
            return emptyList()
        }
        val usersSnapshot = firestore.collection("users")
            .whereIn("username", usernames)
            .get()
            .await()

        return usersSnapshot.documents.mapNotNull { it.toObject(User::class.java) }
    }

    suspend fun getPendingFriendships(currentUsername: String): List<Friendship> {
        val pendingSentSnapshot = firestore.collection("friends")
            .whereEqualTo("status", "pending")
            .whereEqualTo("fromUserUID", currentUsername)
            .get()
            .await()

        val pendingReceivedSnapshot = firestore.collection("friends")
            .whereEqualTo("status", "pending")
            .whereEqualTo("toUserUID", currentUsername)
            .get()
            .await()

        return (pendingSentSnapshot.documents + pendingReceivedSnapshot.documents)
            .mapNotNull { it.toObject(Friendship::class.java) }
    }

    suspend fun removeFriend(currentUserId: String, friendUserId: String) {
        val query1 = firestore.collection("friends")
            .whereEqualTo("fromUserId", currentUserId)
            .whereEqualTo("toUserUID", friendUserId)
            .get()
            .await()

        val query2 = firestore.collection("friends")
            .whereEqualTo("fromUserId", friendUserId)
            .whereEqualTo("toUserUID", currentUserId)
            .get()
            .await()

        val allDocs = query1.documents + query2.documents

        for (doc in allDocs) {
            firestore.collection("friends").document(doc.id).delete().await()
        }
    }

    suspend fun sendFriendRequest(fromUserUID: String, toUserUID: String) {
        if (fromUserUID.isBlank() || toUserUID.isBlank()) {
            throw Exception("Error: fromUserUID or toUserUID is empty")
        }

        val friendship = mapOf(
            "fromUserUID" to fromUserUID,
            "toUserUID" to toUserUID,
            "status" to "pending",
            "timestamp" to Timestamp.now()
        )

        try {
            firestore.collection("friends")
                .add(friendship)
                .await()
        } catch (e: Exception) {
            throw Exception("Error sending the request", e)
        }
    }

    suspend fun acceptFriend(friendship: Friendship) {
        try {
            val query = firestore.collection("friends")
                .whereEqualTo("fromUserUID", friendship.fromUserId)
                .whereEqualTo("toUserUID", friendship.toUserId)
                .whereEqualTo("status", "pending")
                .get()
                .await()

            for (doc in query.documents) {
                doc.reference.update("status", "accepted").await()
            }
        } catch (e: Exception) {
            throw Exception("Error accepting friend request", e)
        }
    }

    suspend fun declineFriend(friendship: Friendship) {
        try {
            val query = firestore.collection("friends")
                .whereEqualTo("fromUserUID", friendship.fromUserId)
                .whereEqualTo("toUserUID", friendship.toUserId)
                .whereEqualTo("status", "pending")
                .get()
                .await()

            for (doc in query.documents) {
                doc.reference.delete().await()
            }
        } catch (e: Exception) {
            throw Exception("Error declining friend request", e)
        }
    }

}
