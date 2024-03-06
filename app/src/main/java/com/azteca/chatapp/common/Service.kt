package com.azteca.chatapp.common

import com.azteca.chatapp.data.model.UserModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Service {


    companion object {
        private const val collectUser = "user"
        private const val collectChatsR = "chatRoom"
        private const val collectChat = "chats"
        const val dbUsername = "username"
        const val dbListUser = "listUser"
        const val dbTimestamp = "timestamp"

        fun getFirebaseAuth(): FirebaseAuth {
            return FirebaseAuth.getInstance()
        }

        fun getCurrentUid(): String? {
            return FirebaseAuth.getInstance().uid
        }

        fun collectionUser(): CollectionReference {
            return FirebaseFirestore.getInstance().collection(collectUser)
        }

        fun getInfUser(userId: String): DocumentReference {
            return FirebaseFirestore.getInstance().collection(collectUser).document(userId)
        }

        fun setInfUser(userId: String, userModel: UserModel): Task<Void> {
            return FirebaseFirestore.getInstance().collection(collectUser).document(userId)
                .set(userModel)
        }

        fun getChatroom(chatRoomId: String): DocumentReference {
            return FirebaseFirestore.getInstance().collection(collectChatsR).document(chatRoomId)
        }

        fun getChatroomMsg(chatRoomId: String): CollectionReference {
            return getChatroom(chatRoomId).collection(collectChat)
        }

        fun getChatroomId(userId1: String, userId2: String): String {
            return if (userId1.hashCode() < userId2.hashCode()) {
                userId1 + "_" + userId2
            } else {
                userId2 + "_" + userId1
            }
        }

        fun getChatroomCollections(): CollectionReference {
            return FirebaseFirestore.getInstance().collection(collectChatsR)
        }

        fun getOtherUserFromChatRoom(listUsers: List<String>): DocumentReference {
            return if (listUsers[0] == getCurrentUid()) {
                collectionUser().document(listUsers[1])
            } else {
                collectionUser().document(listUsers[0])
            }
        }

        fun timeToTime(timestamp: Date): String {
            return SimpleDateFormat("HH:MM", Locale.getDefault()).format(timestamp)
        }

    }
}