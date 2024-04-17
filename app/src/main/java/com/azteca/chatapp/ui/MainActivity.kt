package com.azteca.chatapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.azteca.chatapp.common.Service.Companion.getCurrentUid
import com.azteca.chatapp.common.Service.Companion.getInfUser
import com.azteca.chatapp.databinding.ActivityMainBinding
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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

        getFmcToken()
    }

    private fun getFmcToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                Log.e("main", it.result)
                txtFmcToken = it.result
                getCurrentUid()?.let { it1 -> getInfUser(it1).update("fcmToken", it.result) }
            }
        }
    }
}