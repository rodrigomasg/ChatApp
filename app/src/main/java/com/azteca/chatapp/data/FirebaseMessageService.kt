package com.azteca.chatapp.data

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class FirebaseMessageService @Inject constructor(
    private val firebaseMessage: FirebaseMessaging
) {

    suspend fun getFcmDevice(): String {
        return suspendCancellableCoroutine { continuation ->
            firebaseMessage.token.addOnCompleteListener { task ->
                continuation.resume(if (task.isSuccessful) task.result else "")
            }
        }
    }
}