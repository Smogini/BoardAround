package com.boardaround.firebase

import android.content.Context
import android.net.Uri
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

    private val firestore = FirebaseFirestore.getInstance()

    /**
     * Upload an image to Firebase Storage.
     *
     * @param imageUri The URI of the image to be loaded.
     * @param context of the application.
     * @param onSuccess Callback called if successful, with the image download URL.
     * @param onError Callback called in case of error, with the exception.
     */
    fun uploadImageToFirebase(
        imageUri: Uri,
        context: Context,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val storageRef = Firebase.storage.reference
        val fileName = "profile_pictures/${System.currentTimeMillis()}.jpg"
        val imageRef = storageRef.child(fileName)

        var inputStream: InputStream? = null

        try {
            inputStream = context.contentResolver.openInputStream(imageUri)

            if (inputStream == null) {
                throw Exception("Couldn't get input flow from URI")
            }

            imageRef.putStream(inputStream)
                .addOnSuccessListener { _ ->
                    imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        onSuccess(downloadUri.toString())
                    }.addOnFailureListener { exception ->
                        onError(exception)
                    }
                }
                .addOnFailureListener { exception ->
                    onError(exception)
                }
        } catch (e: Exception) {
            onError(e)
        } finally {
            try {
                inputStream?.close()
            } catch (e: Exception) {
                onError(e)
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

        userRef.set(user).addOnSuccessListener {
        }.addOnFailureListener { e ->
            throw e
        }
    }
}