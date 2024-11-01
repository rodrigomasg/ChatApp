package com.azteca.chatapp.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.azteca.chatapp.R
import com.azteca.chatapp.common.Service
import com.azteca.chatapp.common.Service.Companion.dbTimestamp
import com.azteca.chatapp.common.Service.Companion.getCurrentUid
import com.azteca.chatapp.common.SharedPrefs
import com.azteca.chatapp.data.model.ChatroomModelResponse
import com.azteca.chatapp.data.model.UserModelResponse
import com.azteca.chatapp.databinding.FragmentChatsBinding
import com.azteca.chatapp.ui.adapter.HomeChatAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatsFragment : Fragment() {
    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!
    private var adapter: HomeChatAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        SharedPrefs(requireContext()).setValueLogin(true)
        initListeners()
        initComponets()
    }

    private fun initListeners() {
        binding.mainIvSearch.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
        binding.mainIvProfile.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }
    }

    private fun initComponets() {
        if (getCurrentUid() != null) {
            val query = Service.getChatroomCollections()
                .whereArrayContains(Service.dbListUser, getCurrentUid()!!)
                .orderBy(dbTimestamp, Query.Direction.DESCENDING)

            val opts = FirestoreRecyclerOptions
                .Builder<ChatroomModelResponse>()
                .setQuery(query, ChatroomModelResponse::class.java)
                .build()
            adapter = HomeChatAdapter(opts) { toChat(it) }
            binding.mainRv.layoutManager = LinearLayoutManager(requireContext())
            binding.mainRv.adapter = adapter
            adapter!!.startListening()
        }
    }

    fun toChat(it: UserModelResponse) {
        if (it.userId != null) {
            findNavController().navigate(
                ChatsFragmentDirections.actionChatsFragmentToChatFragment(
                    it.userId!!, it.username, it.phone
                )
            )
        }
    }


    override fun onStop() {
        super.onStop()
        if (adapter != null) adapter!!.stopListening()
    }

    override fun onResume() {
        super.onResume()
        if (adapter != null) adapter!!.notifyDataSetChanged()
    }

}