package com.azteca.chatapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azteca.chatapp.R
import com.azteca.chatapp.data.model.ChatroomModelResponse
import com.azteca.chatapp.data.model.UserModelResponse
import com.azteca.chatapp.databinding.ItemChatHomeBinding
import com.azteca.chatapp.ui.main.fragment.chats.ChatsViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import java.text.SimpleDateFormat
import java.util.Locale

class HomeChatAdapter(
    private val uuid: String,
    options: FirestoreRecyclerOptions<ChatroomModelResponse>,
    private val viewModel: ChatsViewModel,
    private val itemListener: (UserModelResponse) -> Unit
) : FirestoreRecyclerAdapter<ChatroomModelResponse, HomeChatAdapter.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_chat_home, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: ChatroomModelResponse) {

        holder.bind(model, itemListener, uuid, viewModel)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemChatHomeBinding.bind(view)

        @SuppressLint("SetTextI18n")
        fun bind(
            model: ChatroomModelResponse,
            itemListener: (UserModelResponse) -> Unit,
            uuid: String,
            viewModel: ChatsViewModel
        ) {
            viewModel.getOtherUserFromChatRoom(
                uuid = uuid,
                listUser = model.listUser,
                responseUser = { userModel ->
                    model.timestamp?.let { it1 ->
                        binding.itemTvTime.text =
                            SimpleDateFormat("HH:MM", Locale.getDefault()).format(it1)
                    }

                    binding.itemTvUsername.text = userModel?.username ?: ""
                    binding.itemTvLastMsg.text =
                        if (model.lastMsgSenderId == uuid) " Yo: ${model.lastMsg}" else model.lastMsg
                    itemView.setOnClickListener { userModel?.let { it1 -> itemListener(it1) } }
                },
                responseImg = { url ->
                    Glide.with(itemView.context)
                        .load(url)
                        .apply(RequestOptions.circleCropTransform())
                        .into(binding.itemIvUser)
                })
        }
    }

}