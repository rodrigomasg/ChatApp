package com.azteca.chatapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azteca.chatapp.data.AuthFirebaseService
import com.azteca.chatapp.data.FirebaseMessageService
import com.azteca.chatapp.data.FirestoreFirebaseService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val firebaseMessageService: FirebaseMessageService,
    private val firestore: FirestoreFirebaseService,
    private val authFirebaseService: AuthFirebaseService,
) : ViewModel() {

    fun getFcm(response: (String) -> Unit) {
        viewModelScope.launch {
            val resToken = firebaseMessageService.getFcmDevice()
            if (resToken.isNotEmpty()) {
                val uuid = authFirebaseService.getCurrentUid()
                firestore.getInfUser(uuid.toString()).update("fcmToken", resToken)
                response(resToken)
            }
        }
    }

    fun getUuId(response: (String) -> Unit) {
        viewModelScope.launch {
            response(authFirebaseService.getCurrentUid().toString())
        }
    }


}