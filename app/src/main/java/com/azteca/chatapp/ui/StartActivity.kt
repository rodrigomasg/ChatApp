package com.azteca.chatapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.azteca.chatapp.R
import com.azteca.chatapp.common.SharedPrefs
import com.azteca.chatapp.ui.main.MainActivity.Companion.keyNotify
import com.azteca.chatapp.ui.main.MainActivity.Companion.userIdNotify
import com.azteca.chatapp.ui.login.LoginActivity
import com.azteca.chatapp.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val screen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        screen.setKeepOnScreenCondition { false }


        if (SharedPrefs(this).getValueLogin()) {
            val intent = Intent(this, MainActivity::class.java)
            if (intent.extras != null) {
                val userId = intent.extras!!.getString("userId")
                if (!userId.isNullOrEmpty()) {
                    userIdNotify = userId.toString()
                    intent.putExtra(keyNotify, true)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    Log.e("start", "is notify $userId")
                }
            } else {
                intent.putExtra(keyNotify, false)
                Log.e("start", "is not notify")
            }
            startActivity(intent)
            finish()
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}