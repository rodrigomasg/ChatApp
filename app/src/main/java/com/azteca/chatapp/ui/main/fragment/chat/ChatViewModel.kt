package com.azteca.chatapp.ui.main.fragment.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azteca.chatapp.data.AuthFirebaseService
import com.azteca.chatapp.data.FirestoreFirebaseService
import com.azteca.chatapp.data.model.ChatMsgModel
import com.azteca.chatapp.data.model.ChatMsgModelResponse
import com.azteca.chatapp.data.model.ChatroomModel
import com.azteca.chatapp.data.model.ChatroomModelResponse
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.sql.Timestamp
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val firestore: FirestoreFirebaseService,
    private val authFirebaseService: AuthFirebaseService
) : ViewModel() {

    fun getChatRoomId(
        otherUserId: String,
        response: (String) -> Unit,
        responseImg: (String) -> Unit
    ) {
        viewModelScope.launch {
            response(
                firestore.getChatroomId(
                    authFirebaseService.getCurrentUid().toString(), otherUserId
                )
            )

            firestore.refImgProfileUser(otherUserId).downloadUrl.addOnCompleteListener { ref ->
                if (ref.isSuccessful) {
                    responseImg(ref.result.toString())
                }
            }
        }
    }

    fun getChatroom(
        chatroomId: String,
        otherUserId: String,
        chatRoomRes: (ChatroomModelResponse?) -> Unit
    ) {
        viewModelScope.launch {

            firestore.getChatroom(chatroomId).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result.exists()) {
                        chatRoomRes(it.result.toObject(ChatroomModelResponse::class.java))
                    } else {
                        val chatSend = ChatroomModel(
                            chatroomId,
                            listOf(authFirebaseService.getCurrentUid().toString(), otherUserId),
                            Timestamp(System.currentTimeMillis()),
                            "",
                            ""
                        )
                        setChatroom(chatroomId, chatSend, false)
                    }
                }
            }

        }
    }

    fun setChatroom(chatroomId: String, chatSend: ChatroomModel, valSendMsg: Boolean) {
        viewModelScope.launch {
            if (valSendMsg) {
                val mChat = ChatroomModel(
                    chatroomId = chatroomId,
                    listUser = listOf(
                        authFirebaseService.getCurrentUid().toString(),
                        chatSend.listUser[1]
                    ),
                    timestamp = Timestamp(System.currentTimeMillis()),
                    lastMsgSenderId = authFirebaseService.getCurrentUid().toString(),
                    lastMsg = chatSend.lastMsg
                )
                firestore.getChatroom(chatroomId).set(mChat)
            } else {
                firestore.getChatroom(chatroomId).set(chatSend)
            }
        }
    }

    fun getChatRoomMsg(
        chatroomId: String,
        chatMsgModel: ChatMsgModel,
        response: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            chatMsgModel.senderId = authFirebaseService.getCurrentUid().toString()
            firestore.getChatroomMsg(chatroomId).add(chatMsgModel).addOnCompleteListener {
                response(it.isSuccessful)
            }
        }
    }

    fun getChatRoomMsgForAdapter(
        chatroomId: String,
        res: (FirestoreRecyclerOptions<ChatMsgModelResponse>) -> Unit
    ) {
        viewModelScope.launch {
            val query = firestore.getChatroomMsg(chatroomId).orderBy(
                FirestoreFirebaseService.dbTimestamp,
                Query.Direction.DESCENDING
            )

            res(
                FirestoreRecyclerOptions
                    .Builder<ChatMsgModelResponse>()
                    .setQuery(query, ChatMsgModelResponse::class.java)
                    .build()
            )
        }
    }

}