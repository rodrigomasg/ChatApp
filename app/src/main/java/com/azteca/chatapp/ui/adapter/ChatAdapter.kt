package com.azteca.chatapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.azteca.chatapp.R
import com.azteca.chatapp.data.model.ChatMsgModelResponse
import com.azteca.chatapp.databinding.ItemUserMsgBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ChatAdapter(
    private val uuid: String,
    options: FirestoreRecyclerOptions<ChatMsgModelResponse>
) : FirestoreRecyclerAdapter<ChatMsgModelResponse, ChatAdapter.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_msg, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: ChatMsgModelResponse) {
        holder.bind(model, uuid)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemUserMsgBinding.bind(view)
        fun bind(model: ChatMsgModelResponse, uuid: String) {
            if (model.senderId == uuid) {
                binding.msgCvRight.isVisible = true
                binding.msgTvRight.text = model.msg
            } else {
                binding.msgCvLeft.isVisible = true
                binding.msgTvLeft.text = model.msg
            }
        }
    }
}