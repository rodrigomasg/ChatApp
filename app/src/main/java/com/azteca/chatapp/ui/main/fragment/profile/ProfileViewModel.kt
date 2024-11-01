package com.azteca.chatapp.ui.main.fragment.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azteca.chatapp.data.AuthFirebaseService
import com.azteca.chatapp.data.FirestoreFirebaseService
import com.azteca.chatapp.data.model.UserModel
import com.azteca.chatapp.data.model.UserModelResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firestore: FirestoreFirebaseService,
    private val authFirebaseService: AuthFirebaseService
) : ViewModel() {

    fun getUser(response: (UserModelResponse?) -> Unit, urlImage: (String) -> Unit) {
        viewModelScope.launch {
            val resUuid = authFirebaseService.getCurrentUid()
            if (resUuid != null) {
                firestore.getInfUser(resUuid).get().addOnCompleteListener { res ->
                    val userModel = res.result.toObject(UserModelResponse::class.java)
                    response(userModel)
                }
                firestore.refImgProfileUser(resUuid).downloadUrl.addOnCompleteListener { res ->
                    if (res.isSuccessful) {
                        urlImage(res.result.toString())
                    }
                }
            }
        }
    }

    fun updateUser(sendModel: UserModel, uriImage: Uri?, resUpdate: (Boolean) -> Unit) {
        viewModelScope.launch {
            val resUuid = authFirebaseService.getCurrentUid()
            if (resUuid != null) {
                firestore.getInfUser(resUuid).set(sendModel).addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (uriImage != null) {
                            firestore.refImgProfileUser(resUuid).putFile(uriImage)
                                .addOnCompleteListener { resImg ->
                                    resUpdate(resImg.isSuccessful)
                                }
                        } else {
                            resUpdate(true)
                        }
                    }
                }
            }
        }
    }

    fun logOut(response: () -> Unit) {
        viewModelScope.launch {
            authFirebaseService.logOut()
            response()
        }
    }
}