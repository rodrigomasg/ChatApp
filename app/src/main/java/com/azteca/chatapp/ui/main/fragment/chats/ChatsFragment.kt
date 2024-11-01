package com.azteca.chatapp.ui.main.fragment.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.azteca.chatapp.R
import com.azteca.chatapp.common.SharedPrefs
import com.azteca.chatapp.data.model.UserModelResponse
import com.azteca.chatapp.databinding.FragmentChatsBinding
import com.azteca.chatapp.ui.adapter.HomeChatAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatsFragment : Fragment() {
    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChatsViewModel by viewModels()
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
        viewModel.getChats { opts ->
            adapter = HomeChatAdapter(
                uuid = SharedPrefs(requireContext()).getUuid(),
                options = opts,
                viewModel = viewModel,
                itemListener = { toChat(it) }
            )
            adapter!!.startListening()
        }

        binding.mainRv.layoutManager = LinearLayoutManager(requireContext())
        binding.mainRv.adapter = adapter
    }

    private fun toChat(it: UserModelResponse) {
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