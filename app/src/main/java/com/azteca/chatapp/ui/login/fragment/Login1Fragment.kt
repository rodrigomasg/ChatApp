package com.azteca.chatapp.ui.login.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.azteca.chatapp.R
import com.azteca.chatapp.databinding.FragmentLogin1Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Login1Fragment : Fragment() {
    private var _binding: FragmentLogin1Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogin1Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginCodeCountry.registerCarrierNumberEditText(binding.loginEtNumber)
        binding.loginBtnSend.setOnClickListener {
            if (!binding.loginCodeCountry.isValidFullNumber) {
                binding.loginEtNumber.error = getString(R.string.login_err_number)
            } else {
                findNavController().navigate(
                    Login1FragmentDirections.actionLogin1FragmentToLogin2Fragment(binding.loginCodeCountry.fullNumberWithPlus)
                )
            }
        }
    }
}