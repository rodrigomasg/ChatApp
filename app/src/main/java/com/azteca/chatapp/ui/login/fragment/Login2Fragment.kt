package com.azteca.chatapp.ui.login.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.INPUT_METHOD_SERVICE
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.azteca.chatapp.R
import com.azteca.chatapp.databinding.FragmentLogin2Binding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask

private const val TAG = "loginFragment2"

@AndroidEntryPoint
class Login2Fragment : Fragment() {
    private var _binding: FragmentLogin2Binding? = null
    private val binding get() = _binding!!
    private val viewModel: Login2ViewModel by viewModels()
    private val args: Login2FragmentArgs by navArgs()
    private lateinit var txtNumber: String
    private var timerOut = 60L
    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*txtNumber = args.number*/
        /**prueba*/
        txtNumber = "+52 9999999922"
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
        initVM()
    }

    private fun initVM() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loading.collect {
                    binding.login2Pg.isVisible = it
                }
            }
        }
    }

    private fun initComponents() {
        startResendCodeTimer()
        sendCode()
    }

    private fun listeners() {
        binding.loginBtnSend.setOnClickListener {
            if (binding.loginBtnSend.text?.length == 6) {
                viewModel.verifyCode(binding.loginBtnSend.text.toString()) {
                    findNavController().navigate(
                        Login2FragmentDirections.actionLogin2FragmentToLogin3Fragment(txtNumber)
                    )
                }
            }
        }
        binding.loginTvResend.setOnClickListener {
            sendCode()
        }
    }

    private fun startResendCodeTimer() {
        timer = Timer()
        if (timer != null) {
            timer!!.schedule(object : TimerTask() {
                @SuppressLint("SetTextI18n")
                override fun run() {
                    timerOut--
                    requireActivity().runOnUiThread {
                        binding.loginTvResend.isEnabled = false
                        binding.loginTvResend.text =
                            "${getText(R.string.login_resend_code)} $timerOut "
                    }
                    if (timerOut <= 0) {
                        timerOut = 60L
                        timer!!.cancel()
                        requireActivity().runOnUiThread {
                            binding.loginTvResend.isEnabled = true
                        }
                    }
                }
            }, 0, 1000)
        }
    }

    private fun sendCode() {
        viewModel.loginPhone(
            txtNumber,
            requireActivity(),
            onCodeSent = {
                Log.e(TAG, "code sent")
                Toast.makeText(requireContext(), "codigo enviado", Toast.LENGTH_LONG).show()

                /*binding.loginEtNumber.requestFocus()
                val imm =
                    requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.loginEtNumber, InputMethodManager.SHOW_IMPLICIT)*/
            },
            onVerificationCodeComplete = {
                //para pasar directo sin hacer click en comprobar code
                Log.e(TAG, "code completeVerification")
                Toast.makeText(requireContext(), "code sent y complete", Toast.LENGTH_LONG)
                    .show()
                findNavController().navigate(
                    Login2FragmentDirections.actionLogin2FragmentToLogin3Fragment(txtNumber)
                )
            },
            onVerificationFail = { fail ->
                Log.e(TAG, "auth error $fail")
                Toast.makeText(requireContext(), "auth error $fail", Toast.LENGTH_LONG)
                    .show()
            }

        )
    }

    override fun onStop() {
        super.onStop()
        // Detener y limpiar el timer cuando el Fragmento se detiene
        timer?.cancel()
        timer = null
    }
}