package com.azteca.chatapp.common

import android.content.Context
import android.content.SharedPreferences

class SharedPrefs(val context: Context) {
    private val spName = "prefsConf"
    private val spValueLogin = "login"
    private val spValueUuid = "loginId"
    private val storage: SharedPreferences = context.getSharedPreferences(spName, 0)

    fun setValueLogin(valueLogin: Boolean) {
        storage.edit().putBoolean(spValueLogin, valueLogin).apply()
    }

    fun getValueLogin() = storage.getBoolean(spValueLogin, false)

    fun setUuid(uuid: String) {
        storage.edit().putString(spValueUuid, uuid).apply()
    }

    fun getUuid() = storage.getString(spValueUuid, "").toString()

}