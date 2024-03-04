package com.azteca.chatapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.azteca.chatapp.R
import com.azteca.chatapp.common.Service.Companion.firebaseAuth
import com.azteca.chatapp.databinding.FragmentLogin2Binding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit

private const val TAG = "loginFragment2"

class Login2Fragment : Fragment() {
    private var _binding: FragmentLogin2Binding? = null
    private val binding get() = _binding!!
    private val args: Login2FragmentArgs by navArgs()
    private lateinit var txtNumber: String
    private var timerOut = 60L
    private lateinit var forceResendingToken: PhoneAuthProvider.ForceResendingToken
    private var verifyId: String? = null

    private val callBackAuth = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(TAG, "onVerificationCompleted:$credential")
            binding.login2Pg.isVisible = false
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e)
            binding.login2Pg.isVisible = false
            when (e) {
                is FirebaseAuthInvalidCredentialsException -> {
                    // Invalid request
                    Log.w(TAG, "onVerificationFailed", e)
                }

                is FirebaseTooManyRequestsException -> {
                    Log.w(TAG, "onVerificationFailed", e)
                    // The SMS quota for the project has been exceeded
                }

                is FirebaseAuthMissingActivityForRecaptchaException -> {
                    Log.w(TAG, "onVerificationFailed", e)
                    // reCAPTCHA verification attempted with null Activity
                }
            }
            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:$verificationId")
            binding.login2Pg.isVisible = false

            // Save verification ID and resending token so we can use them later
            verifyId = verificationId
            forceResendingToken = token
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        txtNumber = args.number
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogin2Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initComponents()
        listeners()
    }

    private fun initComponents() {
        binding.login2Pg.isVisible = true
        sendCode()
        startResendCodeTimer()
    }

    private fun listeners() {
        binding.loginBtnSend.setOnClickListener {
            if (!binding.loginEtNumber.text.isNullOrEmpty()
                && verifyId != null
            ) {
                val cred = PhoneAuthProvider.getCredential(
                    verifyId!!, binding.loginEtNumber.text.toString()
                )
                signInWithPhoneAuthCredential(cred)
            }
        }
        binding.loginTvResend.setOnClickListener {
            sendCode()
        }
    }

    private fun startResendCodeTimer() {
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            @SuppressLint("SetTextI18n")
            override fun run() {
                timerOut--
                requireActivity().runOnUiThread {
                    binding.loginTvResend.isEnabled = false
                    binding.loginTvResend.text = "${getText(R.string.login_resend_code)} $timerOut "
                }
                if (timerOut <= 0) {
                    timerOut = 60L
                    timer.cancel()
                    requireActivity().runOnUiThread {
                        binding.loginTvResend.isEnabled = true
                    }
                }
            }
        }, 0, 1000)
    }

    private fun sendCode() {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(txtNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(callBackAuth) // OnVerificationStateChangedCallbacks
//            .setForceResendingToken(forceResendingToken)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.e(TAG, "auth success change fragment")
                findNavController().navigate(
                    Login2FragmentDirections.actionLogin2FragmentToLogin3Fragment(txtNumber)
                )
            } else {
                Log.e(TAG, "auth error")
            }
        }
    }


}