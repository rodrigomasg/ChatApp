package com.azteca.chatapp.ui.login.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.azteca.chatapp.R
import com.azteca.chatapp.data.model.UserModel
import com.azteca.chatapp.databinding.FragmentLogin3Binding
import com.azteca.chatapp.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.sql.Timestamp

private const val TAG = "login3"

@AndroidEntryPoint
class Login3Fragment : Fragment() {
    private var _binding: FragmentLogin3Binding? = null
    private val binding get() = _binding!!
    private val viewModel: Login3ViewModel by viewModels()
    private val args: Login3FragmentArgs by navArgs()
    private lateinit var txtNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        txtNumber = args.number
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogin3Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initVm()
    }

    private fun initVm() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loading.collect {
                    binding.login3Pg.isVisible = it
                }
            }
        }
    }

    private fun initListeners() {
        binding.loginBtnSend.setOnClickListener { setUsername() }
    }

    private fun setUsername() {
        val txtUsername = binding.loginEtNumber.text
        if (txtUsername.isNullOrEmpty()) {
            binding.loginEtNumber.error = getString(R.string.login_username_input)
        } else {
            Log.e(TAG, "se creara")
            val userModel = UserModel(
                "",
                txtNumber,
                txtUsername.toString(),
                Timestamp(System.currentTimeMillis()),
                ""
            )
            viewModel.validateInfUser(userModel) {
                Log.e(TAG, it.toString())
                if (it) {
                    requireActivity().finish()
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                }
            }
        }
    }
}