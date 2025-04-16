package com.boardaround

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.util.Log
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.boardaround.R // Sostituisci con il nome del tuo package
import com.boardaround.MainActivity // Sostituisci con la tua Activity principale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {

    // (Opzionale) Se usi Koin:
    // private val userViewModel: UserViewModel by inject()

    override fun onNewToken(token: String) {
        Log.d(TAG, "onNewToken chiamato")
        Log.d(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        token?.let {
            Log.d(TAG, "Sending FCM registration token to server: $it")
            // Per ora, inviamo solo il token al backend per il logging.
            // In futuro, qui chiameremo userViewModel.saveFcmToken(it)
            // CoroutineScope(Dispatchers.IO).launch {
            //     userViewModel.saveFcmToken(it)
            // }
            // TODO: Invia il token al tuo backend (implementa la chiamata API)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        remoteMessage.notification?.let {
            Log.d(TAG, "onMessageReceived chiamato")
            Log.d(TAG, "Message Notification Body: ${it.body}")
            showNotification(it.title ?: "Nuova notifica", it.body ?: "")
        }

        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            // Per ora, non gestiamo i dati.
            // handleNow(remoteMessage.data)
        }
    }

    // (Per ora, vuota)
    // private fun handleNow(data: Map<String, String>) {
    //     Log.d(TAG, "Handling data: $data")
    // }

    private fun showNotification(title: String, messageBody: String) {
        val intent = Intent(this, MainActivity::class.java) // Sostituisci con la tua Activity principale
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_IMMUTABLE) // Usa FLAG_IMMUTABLE o FLAG_UPDATE_CURRENT

        val channelId = getString(R.string.default_notification_channel_id) // Crea una risorsa stringa
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo) // Sostituisci con la tua icona
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "MyFirebaseMessagingService"
    }
}