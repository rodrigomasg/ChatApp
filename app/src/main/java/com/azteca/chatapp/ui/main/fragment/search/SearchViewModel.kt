package com.azteca.chatapp.ui.main.fragment.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azteca.chatapp.data.FirestoreFirebaseService
import com.azteca.chatapp.data.model.UserModelResponse
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val firestore: FirestoreFirebaseService,
) : ViewModel() {

    fun querySearch(
        txtUsername: String,
        response: (FirestoreRecyclerOptions<UserModelResponse>) -> Unit
    ) {
        viewModelScope.launch {
            val query = firestore.collectionUser()
                .whereGreaterThanOrEqualTo(FirestoreFirebaseService.dbUsername, txtUsername)
                .whereLessThanOrEqualTo(FirestoreFirebaseService.dbUsername, txtUsername + '\uf8ff')

            response(
                FirestoreRecyclerOptions.Builder<UserModelResponse>()
                    .setQuery(query, UserModelResponse::class.java)
                    .build()
            )
        }
    }

    fun getImg(s: String, function: (String) -> Unit) {
        firestore.refImgProfileUser(s).downloadUrl.addOnCompleteListener { ref ->
            if (ref.isSuccessful) {
                function(ref.result.toString())
            }
        }
    }

}