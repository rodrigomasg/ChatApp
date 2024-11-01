package com.azteca.chatapp.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.azteca.chatapp.common.SharedPrefs
import com.azteca.chatapp.databinding.ActivityLoginBinding
import com.azteca.chatapp.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        if (SharedPrefs(this).getValueLogin()) {
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}