package com.azteca.chatapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.azteca.chatapp.databinding.FragmentLogin3Binding


class Login3Fragment : Fragment() {
    private var _binding: FragmentLogin3Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogin3Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

}