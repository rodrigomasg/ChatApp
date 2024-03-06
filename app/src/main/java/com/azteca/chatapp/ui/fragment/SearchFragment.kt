package com.azteca.chatapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.azteca.chatapp.common.Service.Companion.dbUsername
import com.azteca.chatapp.common.Service.Companion.firestoreAllUser
import com.azteca.chatapp.data.model.UserModelResponse
import com.azteca.chatapp.databinding.FragmentSearchBinding
import com.azteca.chatapp.ui.adapter.SearchAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions


private const val TAG = "searchFragment"

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var adapter: SearchAdapter? = null
    private val searchUser = object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
        override fun onQueryTextChange(newText: String?): Boolean {
            if (!newText.isNullOrEmpty()) searchQuery(newText)
            return true
        }

        override fun onQueryTextSubmit(query: String?): Boolean {
            if (!query.isNullOrEmpty()) searchQuery(query)
            return true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        binding.searchIvBack.setOnClickListener { parentFragmentManager.popBackStack() }
        binding.searchSearchView.setOnQueryTextListener(searchUser)
    }

    private fun searchQuery(txtUsername: String) {
        val query = firestoreAllUser.whereGreaterThanOrEqualTo(dbUsername, txtUsername)
        val opts = FirestoreRecyclerOptions
            .Builder<UserModelResponse>()
            .setQuery(query, UserModelResponse::class.java)
            .build()
        adapter = SearchAdapter(opts) { toChat() }
        binding.searchRv.layoutManager = LinearLayoutManager(requireContext())
        binding.searchRv.adapter = adapter
        adapter!!.startListening()
    }

    private fun toChat() {
        TODO("Not yet implemented")
    }

    override fun onStop() {
        super.onStop()
        if (adapter != null) adapter!!.stopListening()
    }

    override fun onResume() {
        super.onResume()
        if (adapter != null) adapter!!.startListening()
    }
}