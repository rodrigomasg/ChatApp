package com.azteca.chatapp.data.model

import java.sql.Timestamp

data class ChatroomModel(
    var chatroomId: String,
    var listUser: List<String>,
    var timestamp: Timestamp,
    var lastMsgSenderId: String,
)

data class ChatroomModelResponse(
    var chatroomId: String,
    var listUser: List<String>,
    var timestamp: java.util.Date?,
    var lastMsgSenderId: String,
) {
    constructor() : this("", emptyList(), null, "")
}