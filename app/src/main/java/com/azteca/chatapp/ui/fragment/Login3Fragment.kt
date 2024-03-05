package com.azteca.chatapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.azteca.chatapp.R
import com.azteca.chatapp.common.Service.Companion.firestoreUsers
import com.azteca.chatapp.data.UserModel
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
            val userModel = UserModel(
                txtNumber,
                txtUsername.toString(),
                Timestamp(System.currentTimeMillis())
            )
            firestoreUsers?.set(userModel)?.addOnCompleteListener {
                if (it.isSuccessful) {
                    val main = Intent(requireContext(), MainActivity::class.java)
                    startActivity(main)
                } else {
                    Log.e(TAG, it.result.toString())
                }
            }
        }
    }

    private fun getUsername() {
        firestoreUsers?.get()?.addOnCompleteListener {
            if (it.isSuccessful) {
                val user = it.result.toObject(UserModel::class.java)
                binding.loginEtNumber.setText(user?.username ?: "")
            }
        }
    }

}