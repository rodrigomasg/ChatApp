package com.azteca.chatapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azteca.chatapp.R
import com.azteca.chatapp.common.Service
import com.azteca.chatapp.data.model.UserModelResponse
import com.azteca.chatapp.databinding.ItemUserBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class SearchAdapter(
    options: FirestoreRecyclerOptions<UserModelResponse>,
    private val itemListener: (UserModelResponse) -> Unit
) : FirestoreRecyclerAdapter<UserModelResponse, SearchAdapter.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: UserModelResponse) {

        holder.bind(model, itemListener)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemUserBinding.bind(view)

        @SuppressLint("SetTextI18n")
        fun bind(model: UserModelResponse, itemListener: (UserModelResponse) -> Unit) {
            binding.itemTvUsername.text =
                if (Service.currentUid == model.userId) model.username + itemView.context.getString(
                    R.string.search_item_me
                ) else model.username
            binding.itemTvNumber.text = model.phone
            itemView.setOnClickListener { itemListener(model) }
        }
    }
}