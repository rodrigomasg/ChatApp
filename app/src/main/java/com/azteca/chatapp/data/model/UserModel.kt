package com.azteca.chatapp.data.model

import java.sql.Timestamp
import java.util.Date

data class UserModel(
    var userId: String?,
    var phone: String,
    var username: String,
    var timestamp: Timestamp?,
)

data class UserModelResponse(
    var userId: String?,
    var phone: String,
    var username: String,
    val timestamp: Date? = null
) {
    constructor() : this("", "", "", null)
}