package com.azteca.chatapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.azteca.chatapp.R
import com.azteca.chatapp.common.Service.Companion.isLogin

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val screen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        screen.setKeepOnScreenCondition { false }


        if (isLogin) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}