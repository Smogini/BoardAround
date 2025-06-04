package com.boardaround.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.boardaround.data.repositories.NotificationRepository
import com.boardaround.ui.screens.NotificationItem
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.time.ZoneId

class NotificationViewModel(
    private val notificationRepository: NotificationRepository,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _notifications = mutableStateListOf<NotificationItem>()
    val notifications: List<NotificationItem> = _notifications

    private fun startListeningForRequests(currentUserId: String) {
        firestore.collection("friends")
            .whereEqualTo("toUserId", currentUserId)
            .whereEqualTo("status", "pending")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("NotificationVM", "Errore ascolto", error)
                    return@addSnapshotListener
                }

                _notifications.clear()
                snapshot?.documents?.forEach { doc ->
                    val fromUserId = doc.getString("fromUserId") ?: return@forEach

                    firestore.collection("users").document(fromUserId).get()
                        .addOnSuccessListener { userDoc ->
                            val senderUsername = userDoc.getString("username") ?: "Utente"
                            val id = doc.id
                            val timestamp = Timestamp.now().toDate().toInstant()
                                .atZone(ZoneId.systemDefault()).toLocalDateTime()

                            _notifications.add(
                                NotificationItem(
                                    id = id,
                                    senderUsername = senderUsername,
                                    message = "$senderUsername ti ha inviato una richiesta di amicizia",
                                    timestamp = timestamp
                                )
                            )
                        }
                }
            }
    }

    fun deleteNotification(notification: NotificationItem) {
        _notifications.remove(notification)
    }
}
