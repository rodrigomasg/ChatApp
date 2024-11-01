package com.azteca.chatapp.data

import android.app.Activity
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AuthFirebaseService @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    fun getCurrentUid(): String? {
        /**registro original*/
//        return firebaseAuth.uid
        /**registro de prueba*/
        return "cfvaEqtKwAY3XiAh4R0VO3m9MTH2"
    }

    fun loginPhone(
        toString: String,
        activity: Activity,
        callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        //se debe de agregar el mismo numero de prueba en firebase console
         firebaseAuth.firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+52 9999999922", "222222")

        val options = PhoneAuthOptions
            .newBuilder(firebaseAuth)
            .setPhoneNumber(toString)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callback)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    suspend fun verifyCode(verCode: String, code: String): FirebaseUser? {
        val credential = PhoneAuthProvider.getCredential(verCode, code)
        return completeRegisterCredential(credential)
    }

    suspend fun completeRegisterCredential(
        credential: AuthCredential,
    ): FirebaseUser? {
        return suspendCancellableCoroutine { continuation ->
            firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener {
                    continuation.resume(it.user)
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    fun logOut() {
        /**si es de pueba se comenta*/
        //firebaseAuth.signOut()
    }

}