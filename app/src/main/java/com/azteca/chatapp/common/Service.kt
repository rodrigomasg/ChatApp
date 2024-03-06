package com.azteca.chatapp.common

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class Service {

    companion object {
        private const val collectUser = "user"
        const val dbUsername = "username"


        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUid = Firebase.auth.uid
        val firestoreUsers =
            currentUid?.let {
                FirebaseFirestore.getInstance().collection(collectUser).document(it)
            }
        val firestoreAllUser = FirebaseFirestore.getInstance().collection(collectUser)
        val isLogin = currentUid != null
        fun getInfUser(userId: String): DocumentReference {
            return FirebaseFirestore.getInstance().collection(collectUser).document(userId)
        }
    }
}