package com.azteca.chatapp.common

import android.content.Context
import android.content.SharedPreferences

class SharedPrefs(val context: Context) {
    private val spName = "prefsConf"
    private val spValueLogin = "login"
    private val storage: SharedPreferences = context.getSharedPreferences(spName, 0)

    fun setValueLogin(valueLogin: Boolean) {
        storage.edit().putBoolean(spValueLogin, valueLogin).apply()
    }

    fun getValueLogin(): Boolean {
        return storage.getBoolean(spValueLogin, false)
    }
}