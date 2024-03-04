package com.azteca.chatapp.data

import java.sql.Timestamp

data class UserModel(
    var phone: String,
    var username: String,
    var timestamp: Timestamp,
)