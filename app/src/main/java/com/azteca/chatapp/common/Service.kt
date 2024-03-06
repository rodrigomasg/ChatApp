package com.azteca.chatapp.common

import com.azteca.chatapp.data.model.UserModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class Service {



    companion object {
        private const val collectUser = "user"
        const val dbUsername = "username"

        fun getFirebaseAuth(): FirebaseAuth {
            return FirebaseAuth.getInstance()
        }

        fun getCurrentUid(): String? {
            return Firebase.auth.uid
        }

        fun collectionUser(): CollectionReference {
            return FirebaseFirestore.getInstance().collection(collectUser)
        }

        fun getInfUser(userId: String): DocumentReference {
            return FirebaseFirestore.getInstance().collection(collectUser).document(userId)
        }

        fun setInfUser(userModel: UserModel): Task<Void> {
            return FirebaseFirestore.getInstance().collection(collectUser).document().set(userModel)
        }
    }
}