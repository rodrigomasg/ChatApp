package com.azteca.chatapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.azteca.chatapp.R
import com.azteca.chatapp.common.Service.Companion.currentUid
import com.azteca.chatapp.common.Service.Companion.firestoreUsers
import com.azteca.chatapp.data.model.UserModel
import com.azteca.chatapp.databinding.FragmentLogin3Binding
import com.azteca.chatapp.ui.MainActivity
import java.sql.Timestamp


private const val TAG = "login3"

class Login3Fragment : Fragment() {
    private var _binding: FragmentLogin3Binding? = null
    private val binding get() = _binding!!
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

        initComponents()
        initListeners()
    }

    private fun initComponents() {
        getUsername()
    }

    private fun initListeners() {
        binding.loginBtnSend.setOnClickListener { setUsername() }
    }

    private fun setUsername() {
        val txtUsername = binding.loginEtNumber.text
        if (txtUsername.isNullOrEmpty()) {
            binding.loginEtNumber.error = getString(R.string.login_username_input)
        } else {
            binding.login3Pg.isVisible = true
            val userModel = UserModel(
                currentUid.toString(),
                txtNumber,
                txtUsername.toString(),
                Timestamp(System.currentTimeMillis())
            )
            firestoreUsers?.set(userModel)?.addOnCompleteListener {
                if (it.isSuccessful) {
                    binding.login3Pg.isVisible = false
                    requireActivity().finish()
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                } else {
                    binding.login3Pg.isVisible = false
                    Log.e(TAG, it.result.toString())
                }
            }
        }
    }

    private fun getUsername() {
        binding.login3Pg.isVisible = true
        firestoreUsers?.get()?.addOnCompleteListener {
            if (it.isSuccessful) {
                binding.login3Pg.isVisible = false
                val user = it.result.toObject(UserModel::class.java)
                Log.e(TAG, user?.username ?: "")
                if (!user?.username.isNullOrEmpty()) {
                    requireActivity().finish()
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    binding.loginEtNumber.setText(user?.username ?: "")
                }
            }
        }
    }

}