package com.azteca.chatapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.azteca.chatapp.R

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val screen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        screen.setKeepOnScreenCondition { false }


        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}