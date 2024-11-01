package com.azteca.chatapp.ui.main.fragment.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azteca.chatapp.common.SharedPrefs
import com.azteca.chatapp.data.model.ChatMsgModel
import com.azteca.chatapp.data.model.ChatroomModel
import com.azteca.chatapp.data.model.ChatroomModelResponse
import com.azteca.chatapp.databinding.FragmentChatBinding
import com.azteca.chatapp.ui.adapter.ChatAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import java.sql.Timestamp

private const val TAG = "chatFragment"

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChatViewModel by viewModels()
    private val args: ChatFragmentArgs by navArgs()
    private var chatroomModelResponse: ChatroomModelResponse? = null
    private var adapter: ChatAdapter? = null
    private var chatroomId: String? = null
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
        initAdapter()

        binding.searchIvBack.setOnClickListener { parentFragmentManager.popBackStack() }
        binding.searchIvSend.setOnClickListener { sendMsg() }
    }

    private fun initAdapter() {
        if (chatroomId != null) {
            viewModel.getChatRoomMsgForAdapter(chatroomId!!) { opts ->
                adapter = ChatAdapter(SharedPrefs(requireContext()).getUuid(), opts)
            }

            binding.searchRv.layoutManager = LinearLayoutManager(requireContext()).apply {
                reverseLayout = true
            }
            binding.searchRv.adapter = adapter
            if (adapter != null) {
                adapter!!.startListening()
                adapter!!.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                        super.onItemRangeInserted(positionStart, itemCount)
                        binding.searchRv.smoothScrollToPosition(0)
                    }
                })
            }
        }
    }

    private fun getChatRoomId() {
        if (otherUserId != null) {
            viewModel.getChatRoomId(
                otherUserId!!,
                response = { chat ->
                    chatroomId = chat
                },
                responseImg = { url ->
                    Glide.with(requireContext())
                        .load(url)
                        .apply(RequestOptions.circleCropTransform())
                        .into(binding.searchIvPhoto)
                }
            )

            if (chatroomId != null) {
                viewModel.getChatroom(chatroomId!!, otherUserId!!) {
                    chatroomModelResponse = it
                }
            }
        }
    }

    private fun sendMsg() {
        val txtMsg = binding.loginEtMsg.text.toString().trim()
        if (txtMsg.isNotEmpty()) {
            if (chatroomId != null) {
                val chatSend = ChatroomModel(
                    chatroomId = chatroomId!!,
                    listUser = listOf("", otherUserId!!),
                    timestamp = Timestamp(System.currentTimeMillis()),
                    lastMsgSenderId = "",
                    lastMsg = txtMsg
                )
                viewModel.setChatroom(chatroomId!!, chatSend, true)

                val chatModel = ChatMsgModel(
                    txtMsg,
                    "",
                    Timestamp(System.currentTimeMillis())
                )

                viewModel.getChatRoomMsg(chatroomModelResponse!!.chatroomId, chatModel) {
                    if (it) {
                        binding.loginEtMsg.text = null
                    }
                }
            }
        } else {
            binding.loginEtMsg.error
        }
    }


}