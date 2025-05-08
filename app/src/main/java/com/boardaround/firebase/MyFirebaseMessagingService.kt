package com.boardaround.firebase

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.boardaround.MainActivity
import com.boardaround.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "MyFirebaseMsgService"
    private val CHANNEL_ID = "event_invitation_channel" // ID univoco per il canale di notifica

    /**
     * Chiamato quando viene generato un nuovo token di registrazione FCM.
     * Questo accade in diverse situazioni, tra cui:
     * - La prima volta che l'app viene installata su un dispositivo.
     * - Quando l'utente disinstalla/reinstalla l'app.
     * - Quando l'utente cancella i dati dell'app.
     * - Quando il token esistente scade.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // Invia questo token al tuo server backend o salvalo in un database
        // associato all'utente loggato. Questo token è necessario per inviare
        // notifiche push a questo dispositivo specifico.
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
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Controlla se il messaggio contiene un payload di dati.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")

            // Estrai i dati personalizzati dal payload
            val eventId = remoteMessage.data["event_id"]
            val inviterUsername = remoteMessage.data["inviter_username"]

            // Puoi elaborare questi dati qui (ad esempio, aggiornare la UI, salvare nel database locale)
            // e poi mostrare una notifica all'utente.

            // Mostra una notifica all'utente
            remoteMessage.notification?.let {
                sendNotification(it.title, it.body, eventId, inviterUsername)
            } ?: run {
                // Se non c'è un payload di notifica, crea un titolo e un corpo di default
                sendNotification("Nuovo invito ad un evento!", "Hai ricevuto un nuovo invito.", eventId, inviterUsername)
            }
        }

        // Controlla se il messaggio contiene un payload di notifica.
        // Questo blocco viene eseguito solo se l'app è in primo piano
        // e il messaggio contiene un payload di notifica.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            // Se l'app è in primo piano, puoi mostrare la notifica manualmente
            // (se non l'hai già fatto nel blocco data).
            // sendNotification(it.title, it.body) // Potresti volerlo fare qui se non hai dati
        }
    }

    /**
     * Invia il token di registrazione al tuo server backend.
     * Implementa la logica per inviare il token al tuo backend o database.
     */
    private fun sendRegistrationToServer(token: String) {
        // TODO: Implementa questa funzione per inviare il token al tuo backend
        // Esempio: Utilizza Retrofit, Ktor, o Firebase Firestore/Realtime Database
        // per salvare il token associato all'utente loggato.
        Log.d(TAG, "Invio token al server: $token")
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
        // Crea un Intent che si aprirà quando l'utente clicca sulla notifica
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            // Puoi aggiungere extra all'Intent per passare dati all'Activity
            putExtra("event_id", eventId)
            putExtra("inviter_username", inviterUsername)
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0, // Request code
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT // Flags
        )

        // Crea un Notification Channel (necessario per Android 8.0 e successivi)
        createNotificationChannel()

        // Costruisci la notifica
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification) // Sostituisci con l'icona della tua notifica
            .setContentTitle(messageTitle ?: "Notifica")
            .setContentText(messageBody ?: "Hai ricevuto un nuovo messaggio.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent) // Imposta l'Intent da lanciare al click
            .setAutoCancel(true) // La notifica scompare quando l'utente ci clicca sopra

        // Mostra la notifica
        with(NotificationManagerCompat.from(this)) {
            // Controlla il permesso POST_NOTIFICATIONS per Android 13+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ActivityCompat.checkSelfPermission(
                        this@MyFirebaseMessagingService,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // Se il permesso non è concesso, non mostrare la notifica
                    // In un'app reale, dovresti gestire la richiesta del permesso all'utente
                    Log.w(TAG, "Permesso POST_NOTIFICATIONS non concesso. Impossibile mostrare la notifica.")
                    return
                }
            }
            // notificationId è un ID univoco per ogni notifica che mostri
            val notificationId = System.currentTimeMillis().toInt() // Un ID univoco basato sul timestamp
            notify(notificationId, builder.build())
        }
    }

    /**
     * Crea un Notification Channel.
     * Questo è necessario per mostrare notifiche su Android 8.0 (Oreo) e successivi.
     */
    private fun createNotificationChannel() {
        // Crea il NotificationChannel solo su API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name) // Definisci questo nel tuo strings.xml
            val descriptionText = getString(R.string.channel_description) // Definisci questo nel tuo strings.xml
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Registra il canale con il sistema
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}