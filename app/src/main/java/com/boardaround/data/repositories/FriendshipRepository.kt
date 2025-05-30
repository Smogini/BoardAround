package com.boardaround.data.repositories

import android.util.Log
import com.boardaround.data.entities.Friendship
import com.boardaround.data.entities.User
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FriendshipRepository {

    private val _friends = MutableStateFlow<List<User>>(emptyList())
    val friends = _friends.asStateFlow()

    private val _pendingFriends = MutableStateFlow<List<Friendship>>(emptyList())
    val pendingFriends = _pendingFriends.asStateFlow()

    private val _friendStatuses = MutableStateFlow<Map<String, String>>(emptyMap())


    suspend fun loadFriends(currentUsername: String) {
        val db = FirebaseFirestore.getInstance()

        try {
            val sent = db.collection("friends")
                .whereEqualTo("status", "accepted")
                .whereEqualTo("fromUserId", currentUsername)
                .get()
                .await()

            val received = db.collection("friends")
                .whereEqualTo("status", "accepted")
                .whereEqualTo("toUserId", currentUsername)
                .get()
                .await()

            val allDocs = sent.documents + received.documents

            val friendUsernames = allDocs.mapNotNull { doc ->
                doc.toObject(Friendship::class.java)?.let { fr ->
                    if (fr.fromUserId == currentUsername) fr.toUserId else fr.fromUserId
                }
            }.distinct()

            if (friendUsernames.isNotEmpty()) {
                val usersSnapshot = db.collection("users")
                    .whereIn("username", friendUsernames)
                    .get()
                    .await()

                val users = usersSnapshot.documents.mapNotNull { it.toObject(User::class.java) }
                _friends.value = users
            } else {
                _friends.value = emptyList()
            }

            val pendingSentSnapshot = db.collection("friends")
                .whereEqualTo("status", "pending")
                .whereEqualTo("fromUserId", currentUsername)
                .get()
                .await()

            val pendingReceivedSnapshot = db.collection("friends")
                .whereEqualTo("status", "pending")
                .whereEqualTo("toUserId", currentUsername)
                .get()
                .await()

            val pendingDocs = pendingSentSnapshot.documents + pendingReceivedSnapshot.documents

            val pendingFriendships = pendingDocs.mapNotNull { doc ->
                doc.toObject(Friendship::class.java)
            }

            _pendingFriends.value = pendingFriendships

        } catch (e: Exception) {
            Log.e("FriendshipRepo", "Error loading friends: ${e.message}", e)
        }
    }




    suspend fun removeFriend(currentUserId: String, friendUserId: String) {
        val db = Firebase.firestore

        Log.d("FriendshipRepo", "Removing friendship between $currentUserId and $friendUserId")

        val query1 = db.collection("friends")
            .whereEqualTo("fromUserId", currentUserId)
            .whereEqualTo("toUserId", friendUserId)
            .get()
            .await()

        val query2 = db.collection("friends")
            .whereEqualTo("fromUserId", friendUserId)
            .whereEqualTo("toUserId", currentUserId)
            .get()
            .await()

        val allDocs = query1.documents + query2.documents
        Log.d("FriendshipRepo", "Found ${allDocs.size} friendships to remove")

        for (doc in allDocs) {
            db.collection("friends").document(doc.id).delete().await()
            Log.d("FriendshipRepo", "Deleted friendship document: ${doc.id}")
        }

        loadFriends(currentUserId)
    }

    suspend fun sendFriendRequest(fromUsername: String, toUsername: String) {
        val db = Firebase.firestore

        if (fromUsername.isBlank() || toUsername.isBlank()) {
            Log.e("FriendshipRepo", "Errore: fromUsername o toUsername è vuoto")
            return
        }

        val friendship = mapOf(
            "fromUserId" to fromUsername,
            "toUserId" to toUsername,
            "status" to "pending",
            "timestamp" to Timestamp.now()
        )

        try {
            Log.d("FriendshipRepo", "Sending request from $fromUsername to $toUsername")
            val docRef = db.collection("friends").add(friendship).await()
            Log.d("FriendshipRepo", "Friend request added with id: ${docRef.id}")
        } catch (e: Exception) {
            Log.e("FriendshipRepo", "Errore nell'invio della richiesta", e)
        }
    }


    suspend fun loadFriendsWithStatuses(currentUserUsername: String) {
        val db = Firebase.firestore

        val statuses = listOf("accepted", "pending")
        val friendDocs = mutableListOf<com.google.firebase.firestore.DocumentSnapshot>()

        for (status in statuses) {
            val sent = db.collection("friends")
                .whereEqualTo("status", status)
                .whereEqualTo("fromUserId", currentUserUsername)
                .get()
                .await()

            val received = db.collection("friends")
                .whereEqualTo("status", status)
                .whereEqualTo("toUserId", currentUserUsername)
                .get()
                .await()

            friendDocs.addAll(sent.documents)
            friendDocs.addAll(received.documents)
        }

        val friendUsernameWithStatus = friendDocs.mapNotNull { doc ->
            doc.toObject(Friendship::class.java)?.let { fr ->
                val friendUsername = if (fr.fromUserId == currentUserUsername) fr.toUserId else fr.fromUserId
                friendUsername to fr.status
            }
        }.distinctBy { it.first }

        if (friendUsernameWithStatus.isEmpty()) {
            _friends.value = emptyList()
            _friendStatuses.value = emptyMap()
            return
        }

        val friendUsernames = friendUsernameWithStatus.map { it.first }

        val usersSnapshot = db.collection("users")
            .whereIn(FieldPath.documentId(), friendUsernames)
            .get()
            .await()

        val users = usersSnapshot.documents.mapNotNull { it.toObject(User::class.java) }

        _friends.value = users

        val statusesMap = friendUsernameWithStatus.toMap()
        _friendStatuses.value = statusesMap
    }

    suspend fun acceptFriend(friendship: Friendship) {
        val db = Firebase.firestore
        try {
            val query = db.collection("friends")
                .whereEqualTo("fromUserId", friendship.fromUserId)
                .whereEqualTo("toUserId", friendship.toUserId)
                .whereEqualTo("status", "pending")
                .get()
                .await()

            for (doc in query.documents) {
                doc.reference.update("status", "accepted").await()
                Log.d("FriendshipRepo", "Accepted friend request ${doc.id}")
            }

            loadFriends(friendship.toUserId) // o fromUserId, a seconda di chi è corrente
        } catch (e: Exception) {
            Log.e("FriendshipRepo", "Error accepting friend request", e)
        }
    }

    suspend fun declineFriend(friendship: Friendship) {
        val db = Firebase.firestore
        try {
            val query = db.collection("friends")
                .whereEqualTo("fromUserId", friendship.fromUserId)
                .whereEqualTo("toUserId", friendship.toUserId)
                .whereEqualTo("status", "pending")
                .get()
                .await()

            for (doc in query.documents) {
                doc.reference.delete().await()
                Log.d("FriendshipRepo", "Declined friend request and deleted ${doc.id}")
            }

            loadFriends(friendship.toUserId)
        } catch (e: Exception) {
            Log.e("FriendshipRepo", "Error declining friend request", e)
        }
    }


}
