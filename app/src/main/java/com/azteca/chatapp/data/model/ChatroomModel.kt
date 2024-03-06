package com.azteca.chatapp.data.model

import java.sql.Timestamp

data class ChatroomModel(
    var chatroomId: String,
    var listUser: List<String>,
    var timestamp: Timestamp,
    var lastMsgSenderId: String,
    var lastMsg: String,
)

data class ChatroomModelResponse(
    var chatroomId: String,
    var listUser: List<String>,
    var timestamp: java.util.Date?,
    var lastMsgSenderId: String,
    var lastMsg: String,
) {
    constructor() : this("", emptyList(), null, "", "")
}