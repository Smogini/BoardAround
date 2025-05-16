package com.boardaround.firebase

import android.content.Context
import android.net.Uri
import android.util.Log
import com.boardaround.data.entities.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.ktx.storage
import java.io.InputStream
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object FirebaseUtils {

    private const val TAG = "FirebaseUtils" // Tag per i log
    private val firestore = FirebaseFirestore.getInstance()

    /**
     * Carica un'immagine su Firebase Storage.
     *
     * @param imageUri L'URI dell'immagine da caricare.
     * @param context Il contesto dell'applicazione.
     * @param onSuccess Callback chiamato in caso di successo, con l'URL di download dell'immagine.
     * @param onError Callback chiamato in caso di errore, con l'eccezione.
     */
    fun uploadImageToFirebase(
        imageUri: Uri,
        context: Context,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val storageRef = Firebase.storage.reference
        // Utilizza un timestamp per garantire un nome file univoco
        val fileName = "profile_pictures/${System.currentTimeMillis()}.jpg"
        val imageRef = storageRef.child(fileName)

        var inputStream: InputStream? = null // Dichiarato fuori dal try per poterlo chiudere nel finally

        try {
            // Ottieni il flusso di input dall'URI
            inputStream = context.contentResolver.openInputStream(imageUri)
            Log.d(TAG, "InputStream ottenuto: $inputStream per URI: $imageUri")

            if (inputStream == null) {
                Log.e(TAG, "Impossibile ottenere il flusso di input dall'URI: $imageUri")
                throw Exception("Impossibile ottenere il flusso di input dall'URI")
            }

            // Carica il file tramite InputStream
            imageRef.putStream(inputStream)
                .addOnSuccessListener { taskSnapshot ->
                    Log.d(TAG, "Caricamento immagine riuscito: ${taskSnapshot.metadata?.path}")
                    // Ottieni l'URL di download dell'immagine
                    imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        Log.d(TAG, "URL di download ottenuto: $downloadUri")
                        onSuccess(downloadUri.toString())
                    }.addOnFailureListener { exception ->
                        Log.e(TAG, "Errore nell'ottenere l'URL di download: ${exception.message}", exception)
                        onError(exception)
                    }
                }
                .addOnFailureListener { exception ->
                    // Gestisci l'errore, se si verifica
                    Log.e(TAG, "Errore durante il caricamento dell'immagine: ${exception.message}", exception)
                    onError(exception)
                }
        } catch (e: Exception) {
            // Gestione degli errori generali (es. SecurityException per l'URI)
            Log.e(TAG, "Errore generale durante il caricamento: ${e.message}", e)
            onError(e)
        } finally {
            // Assicurati che l'InputStream venga chiuso
            try {
                inputStream?.close()
            } catch (e: Exception) {
                Log.e(TAG, "Errore nella chiusura dell'InputStream: ${e.message}", e)
            }
        }
    }

    suspend fun getFcmToken(): String = suspendCoroutine { continuation ->
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                continuation.resume(token)
            }
            .addOnFailureListener { e ->
                continuation.resumeWithException(e)
            }
    }


    fun registerUser(user: User) {
        val userRef = firestore.collection("users").document(user.uid)

        // Aggiungi i dati dell'utente a Firestore
        userRef.set(user).addOnSuccessListener {
            // Operazione completata con successo
        }.addOnFailureListener { e ->
            // Errore durante il salvataggio
            throw e
        }
    }
}