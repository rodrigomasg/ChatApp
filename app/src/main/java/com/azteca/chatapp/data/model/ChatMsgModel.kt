package com.azteca.chatapp.data.model

import java.sql.Timestamp
import java.util.Date

data class ChatMsgModel(
    var msg: String,
    var senderId: String,
    var timestamp: Timestamp,
)

data class ChatMsgModelResponse(
    var msg: String,
    var senderId: String,
    var timestamp: Date?,
) {
    constructor() : this("", "", null)
}
