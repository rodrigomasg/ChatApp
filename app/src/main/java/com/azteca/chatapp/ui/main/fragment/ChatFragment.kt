package com.azteca.chatapp.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azteca.chatapp.common.Service
import com.azteca.chatapp.common.Service.Companion.getChatroom
import com.azteca.chatapp.common.Service.Companion.getChatroomId
import com.azteca.chatapp.common.Service.Companion.getChatroomMsg
import com.azteca.chatapp.common.Service.Companion.getCurrentUid
import com.azteca.chatapp.data.model.ChatMsgModel
import com.azteca.chatapp.data.model.ChatMsgModelResponse
import com.azteca.chatapp.data.model.ChatroomModel
import com.azteca.chatapp.data.model.ChatroomModelResponse
import com.azteca.chatapp.databinding.FragmentChatBinding
import com.azteca.chatapp.ui.adapter.ChatAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import dagger.hilt.android.AndroidEntryPoint
import java.sql.Timestamp

private const val TAG = "chatFragment"

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
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
            val query = getChatroomMsg(chatroomId!!).orderBy(
                Service.dbTimestamp,
                Query.Direction.DESCENDING
            )

            val opts = FirestoreRecyclerOptions
                .Builder<ChatMsgModelResponse>()
                .setQuery(query, ChatMsgModelResponse::class.java)
                .build()
            adapter = ChatAdapter(opts)
            binding.searchRv.layoutManager = LinearLayoutManager(requireContext()).apply {
                reverseLayout = true
            }
            binding.searchRv.adapter = adapter
            adapter!!.startListening()
            adapter!!.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    super.onItemRangeInserted(positionStart, itemCount)
                    binding.searchRv.smoothScrollToPosition(0)
                }
            })
        }
    }

    private fun getChatRoomId() {
        if (getCurrentUid() != null && otherUserId != null) {
            chatroomId = getChatroomId(getCurrentUid()!!, otherUserId!!)
            Service.refImgProfileUser(
                otherUserId ?: ""
            ).downloadUrl.addOnCompleteListener { ref ->
                if (ref.isSuccessful) {
                    val uri = ref.result
                    Glide.with(requireContext())
                        .load(uri)
                        .apply(RequestOptions.circleCropTransform())
                        .into(binding.searchIvPhoto)
                }
            }

            if (chatroomId != null) {
                getChatroom(chatroomId!!).get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (it.result.exists()) {
                            chatroomModelResponse =
                                it.result.toObject(ChatroomModelResponse::class.java)
                        } else {
                            if (chatroomModelResponse == null) {
                                val chatSend = ChatroomModel(
                                    chatroomId!!,
                                    listOf(getCurrentUid()!!, otherUserId!!),
                                    Timestamp(System.currentTimeMillis()),
                                    "",
                                    ""
                                )
                                getChatroom(chatroomId!!).set(chatSend)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun sendMsg() {
        val txtMsg = binding.loginEtMsg.text.toString().trim()
        if (txtMsg.isNotEmpty()) {
            if (getCurrentUid() != null
                && chatroomId != null
            ) {
                val chatSend = ChatroomModel(
                    chatroomId = chatroomId!!,
                    listUser = listOf(getCurrentUid()!!, otherUserId!!),
                    timestamp = Timestamp(System.currentTimeMillis()),
                    lastMsgSenderId = getCurrentUid()!!,
                    txtMsg
                )
                getChatroom(chatroomId!!).set(chatSend)

                val chatModel = ChatMsgModel(
                    txtMsg,
                    getCurrentUid()!!,
                    Timestamp(System.currentTimeMillis())
                )
                getChatroomMsg(chatroomModelResponse!!.chatroomId).add(chatModel)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            binding.loginEtMsg.text = null
                        }
                    }
            }
        } else {
            binding.loginEtMsg.error
        }
    }


}