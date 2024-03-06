package com.azteca.chatapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.azteca.chatapp.common.Service.Companion.getInfUser
import com.azteca.chatapp.data.model.UserModelResponse
import com.azteca.chatapp.databinding.FragmentChatBinding

class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val args: ChatFragmentArgs by navArgs()
    private var otherUserId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        otherUserId = args.userId
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponents()
    }

    private fun initComponents() {
        getInfoUser()

        binding.searchIvBack.setOnClickListener { parentFragmentManager.popBackStack() }
        binding.searchIvSend.setOnClickListener { sendMsg() }
    }

    private fun getInfoUser() {
        otherUserId?.let {
            getInfUser(it).get().addOnSuccessListener { doc ->
                if (doc.exists()) {
                    val user = doc.toObject(UserModelResponse::class.java)
                    binding.chatTvUsername.text = user?.username ?: ""
                } else {
                    // no existe
                }
            }
        }
    }

    private fun sendMsg() {

    }


}