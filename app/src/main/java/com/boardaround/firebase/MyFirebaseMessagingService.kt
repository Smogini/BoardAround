package com.boardaround.firebase

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.boardaround.MainActivity
import com.boardaround.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val tag = "MyFirebaseMsgService"
    private val channelId = "event_invitation_channel" // ID univoco per il canale di notifica

    /**
     * Chiamato quando viene generato un nuovo token di registrazione FCM.
     * Questo accade in diverse situazioni, tra cui:
     * - La prima volta che l'app viene installata su un dispositivo.
     * - Quando l'utente disinstalla/reinstalla l'app.
     * - Quando l'utente cancella i dati dell'app.
     * - Quando il token esistente scade.
     */
    override fun onNewToken(token: String) {
        Log.d(tag, "Refreshed token: $token")

        sendRegistrationToServer(token)
    }

    /**
     * Chiamato quando viene ricevuto un messaggio FCM.
     * Questo metodo viene chiamato sia quando l'app è in primo piano che in background.
     * Se l'app è in background e il messaggio contiene solo un payload di notifica,
     * la notifica viene visualizzata automaticamente dal sistema e questo metodo
     * potrebbe non essere chiamato. Se il messaggio contiene un payload di dati,
     * questo metodo viene sempre chiamato.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(tag, "From: ${remoteMessage.from}")

        // Controlla se il messaggio contiene un payload di dati.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(tag, "Message data payload: ${remoteMessage.data}")

            val eventId = remoteMessage.data["event_id"]
            val inviterUsername = remoteMessage.data["inviter_username"]

            remoteMessage.notification?.let {
                sendNotification(it.title, it.body, eventId, inviterUsername)
            } ?: run {
                sendNotification("Nuovo invito ad un evento!", "Hai ricevuto un nuovo invito.", eventId, inviterUsername)
            }
        }

        remoteMessage.notification?.let {
            Log.d(tag, "Message Notification Body: ${it.body}")
            // sendNotification(it.title, it.body)
        }
    }

    /**
     * Invia il token di registrazione al tuo server backend.
     * Implementa la logica per inviare il token al tuo backend o database.
     */
    private fun sendRegistrationToServer(token: String) {
        // TODO
        Log.d(tag, "Invio token al server: $token")
    }

    /**
     * Crea e mostra una notifica all'utente.
     *
     * @param messageTitle Il titolo della notifica.
     * @param messageBody Il corpo della notifica.
     * @param eventId L'ID dell'evento (opzionale, dai dati del messaggio).
     * @param inviterUsername Lo username dell'invitante (opzionale, dai dati del messaggio).
     */
    private fun sendNotification(
        messageTitle: String?,
        messageBody: String?,
        eventId: String? = null,
        inviterUsername: String? = null
    ) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("event_id", eventId)
            putExtra("inviter_username", inviterUsername)
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        createNotificationChannel()

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(messageTitle ?: "Notifica")
            .setContentText(messageBody ?: "Hai ricevuto un nuovo messaggio.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@MyFirebaseMessagingService,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.w(
                    tag,
                    "Permesso POST_NOTIFICATIONS non concesso. Impossibile mostrare la notifica."
                )
                return
            }
            val notificationId = System.currentTimeMillis().toInt()
            notify(notificationId, builder.build())
        }
    }

    /**
     * Crea un Notification Channel.
     * Questo è necessario per mostrare notifiche su Android 8.0 (Oreo) e successivi.
     */
    private fun createNotificationChannel() {
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}