package com.azteca.chatapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.azteca.chatapp.R
import com.azteca.chatapp.common.SharedPrefs
import com.azteca.chatapp.databinding.FragmentChatsBinding

class ChatsFragment : Fragment() {
    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!

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
    }

    private fun initListeners() {
        binding.mainIvSearch.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
        binding.mainIvProfile.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }
    }

}