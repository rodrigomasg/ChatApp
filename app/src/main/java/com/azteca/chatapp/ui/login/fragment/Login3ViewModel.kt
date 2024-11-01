package com.azteca.chatapp.ui.login.fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azteca.chatapp.data.AuthFirebaseService
import com.azteca.chatapp.data.FirestoreFirebaseService
import com.azteca.chatapp.data.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "login3ViewModel"

@HiltViewModel
class Login3ViewModel @Inject constructor(
    private val firebaseService: FirestoreFirebaseService,
    private val authFirebaseService: AuthFirebaseService
) : ViewModel() {

    private var _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun validateInfUser(userModel: UserModel, res: (Boolean) -> Unit) {
        viewModelScope.launch {
            _loading.value = true

            val uuid = authFirebaseService.getCurrentUid()
            Log.d(TAG, "current uuid -> $uuid")
            if (uuid != null) {
                userModel.userId = uuid
                try {
                    val response = firebaseService.setInfUser(uuid, userModel)
                    Log.d(TAG, " set inf user -> $response")
                    res(response)
                } catch (_: Exception) {
                    //res(false)
                }
            }
            _loading.value = false
        }
    }

}