package com.azteca.chatapp.data.model

import java.sql.Timestamp

data class ChatMsgModel(
    var msg: String,
    var senderId: String,
    var timestamp: Timestamp,
)
