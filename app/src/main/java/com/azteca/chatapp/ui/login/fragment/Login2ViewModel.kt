package com.azteca.chatapp.ui.login.fragment

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azteca.chatapp.data.AuthFirebaseService
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "loginViewModel"

@HiltViewModel
class Login2ViewModel @Inject constructor(
    private val authFirebaseService: AuthFirebaseService
) : ViewModel() {

    private var _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
    private lateinit var verCode: String

    /**LoginPhone*/
    fun loginPhone(
        toString: String,
        activity: Activity,
        onCodeSent: () -> Unit,
        onVerificationCodeComplete: () -> Unit,
        onVerificationFail: (String) -> Unit
    ) {
        viewModelScope.launch {
            _loading.value = true

            val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    /**
                     * validara para hacer el registro automatico
                     * lo mismo que verificationcode
                     * */
                    // This callback will be invoked in two situations:
                    // 1 - Instant verification. In some cases the phone number can be instantly
                    //     verified without needing to send or enter a verification code.
                    // 2 - Auto-retrieval. On some devices Google Play services can automatically
                    //     detect the incoming verification SMS and perform verification without
                    //     user action.
                    Log.d(TAG, "onVerificationCompleted:$p0")
                    viewModelScope.launch {
                        /**------------------------------------*/
                        /** le pasamos la lambda ya que no tiene que comprobar credenciales por ser numero de pueba*/
                         onVerificationCodeComplete()
                        /**------------------------------------*/
                        /** le cuando se necesite completar el registro*/
                        /*val res = authFirebaseService.completeRegisterCredential(p0)
                        if (res != null) {
                            onVerificationCodeComplete()
                        }*/
                    }
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    // This callback is invoked in an invalid request for verification is made,
                    // for instance if the the phone number format is not valid.
                    Log.w(TAG, "onVerificationFailed", p0)
                    _loading.value = false
                    onVerificationFail(p0.message.toString())
                    when (p0) {
                        is FirebaseAuthInvalidCredentialsException -> {
                            // Invalid request
                            Log.w(TAG, "onVerificationFailed", p0)
                        }

                        is FirebaseTooManyRequestsException -> {
                            Log.w(TAG, "onVerificationFailed", p0)
                            // The SMS quota for the project has been exceeded
                        }

                        is FirebaseAuthMissingActivityForRecaptchaException -> {
                            Log.w(TAG, "onVerificationFailed", p0)
                            // reCAPTCHA verification attempted with null Activity
                        }
                    }
                }

                override fun onCodeSent(
                    verCode: String,
                    p1: PhoneAuthProvider.ForceResendingToken
                ) {
                    // The SMS verification code has been sent to the provided phone number, we
                    // now need to ask the user to enter the code and then construct a credential
                    // by combining the code with a verification ID.
                    Log.d(TAG, "onCodeSent:$verCode")
                    _loading.value = false
                    this@Login2ViewModel.verCode = verCode
                    onCodeSent()
                }
            }

            authFirebaseService.loginPhone(toString, activity, callback)

            _loading.value = false
        }
    }

    fun verifyCode(code: String, function: () -> Unit) {
        viewModelScope.launch {
            val res = withContext(Dispatchers.IO) {
                authFirebaseService.verifyCode(verCode, code)
            }
            if (res != null) {
                function()
            }
        }
    }

}