package com.azteca.chatapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.azteca.chatapp.common.Service.Companion.getChatroom
import com.azteca.chatapp.common.Service.Companion.getChatroomId
import com.azteca.chatapp.common.Service.Companion.getCurrentUid
import com.azteca.chatapp.data.model.ChatroomModel
import com.azteca.chatapp.data.model.ChatroomModelResponse
import com.azteca.chatapp.databinding.FragmentChatBinding
import java.sql.Timestamp

private const val TAG = "chatFragment"

class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val args: ChatFragmentArgs by navArgs()
    private var chatroomModelResponse: ChatroomModelResponse? = null
    private var otherUserId: String? = null
    private var otherUsername: String? = null
    private var otherNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        otherUserId = args.userId
        otherUsername = args.username
        otherNumber = args.number
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
        binding.chatTvUsername.text = otherUsername ?: ""
        getChatRoomId()

        binding.searchIvBack.setOnClickListener { parentFragmentManager.popBackStack() }
        binding.searchIvSend.setOnClickListener { sendMsg() }
    }

    private fun getChatRoomId() {
        if (getCurrentUid() != null && otherUserId != null) {
            val chatId = getChatroomId(getCurrentUid()!!, otherUserId!!)
            getChatroom(chatId).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result.exists()) {
                        chatroomModelResponse =
                            it.result.toObject(ChatroomModelResponse::class.java)
                    } else {
                        if (chatroomModelResponse == null) {
                            val chatSend = ChatroomModel(
                                chatId,
                                listOf(getCurrentUid()!!, otherUserId!!),
                                Timestamp(System.currentTimeMillis()),
                                ""
                            )
                            getChatroom(chatId).set(chatSend)
                        }
                    }
                }
            }
        }
    }

    private fun sendMsg() {
        val txtMsg = binding.loginEtMsg.text.toString().trim()
        if (!txtMsg.isEmpty()) {

        } else {
            binding.loginEtMsg.error
        }
    }


}