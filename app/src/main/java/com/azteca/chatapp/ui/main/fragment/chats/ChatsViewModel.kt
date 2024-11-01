package com.azteca.chatapp.ui.main.fragment.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azteca.chatapp.data.AuthFirebaseService
import com.azteca.chatapp.data.FirestoreFirebaseService
import com.azteca.chatapp.data.FirestoreFirebaseService.Companion.dbTimestamp
import com.azteca.chatapp.data.model.ChatroomModelResponse
import com.azteca.chatapp.data.model.UserModelResponse
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    private val authFirebaseService: AuthFirebaseService,
    private val firestore: FirestoreFirebaseService
) : ViewModel() {

    fun getChats(opt: (FirestoreRecyclerOptions<ChatroomModelResponse>) -> Unit) {
        viewModelScope.launch {
            val resUuid = authFirebaseService.getCurrentUid()
            if (resUuid != null) {
                val query = firestore.getChatroomCollections()
                    .whereArrayContains(FirestoreFirebaseService.dbListUser, resUuid)
                    .orderBy(dbTimestamp, Query.Direction.DESCENDING)

                opt(
                    FirestoreRecyclerOptions
                        .Builder<ChatroomModelResponse>()
                        .setQuery(query, ChatroomModelResponse::class.java)
                        .build()
                )
            }
        }
    }

    fun getOtherUserFromChatRoom(
        uuid: String,
        listUser: List<String>,
        responseUser: (UserModelResponse?) -> Unit,
        responseImg: (String?) -> Unit,
    ) {
        firestore.getOtherUserFromChatRoom(listUser, uuid).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val mUser = it.result.toObject(UserModelResponse::class.java)
                responseUser(mUser)

                firestore.refImgProfileUser(mUser?.userId.orEmpty()).downloadUrl
                    .addOnCompleteListener { ref ->
                        if (ref.isSuccessful) {
                            responseImg(ref.result.toString())
                        }
                    }
            }

        }
    }
}