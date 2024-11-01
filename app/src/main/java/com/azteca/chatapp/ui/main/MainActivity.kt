package com.azteca.chatapp.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.azteca.chatapp.common.SharedPrefs
import com.azteca.chatapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    companion object {
        var txtFmcToken: String = ""

        /**notifications*/
        var keyNotify: String = "notify"
        var userIdNotify: String = ""

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
    }

    private fun initComponents() {
        SharedPrefs(this).setValueLogin(true)
        getFmcToken()
        getUuId()
        Log.d("mainActivity", "Create")
    }

    private fun getUuId() {
        viewModel.getUuId {
            SharedPrefs(this@MainActivity).setUuid(it)
        }
    }

    private fun getFmcToken() {
        viewModel.getFcm {
            txtFmcToken = it
        }
    }
}