package com.azteca.chatapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.azteca.chatapp.databinding.FragmentLogin2Binding

class Login2Fragment : Fragment() {
    private var _binding: FragmentLogin2Binding? = null
    private val binding get() = _binding!!
    private val args: Login2FragmentArgs by navArgs()
    private lateinit var txtNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        txtNumber = args.number
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogin2Binding.inflate(layoutInflater, container, false)
        return binding.root
    }


}