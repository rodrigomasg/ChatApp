package com.azteca.chatapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azteca.chatapp.R
import com.azteca.chatapp.common.Service
import com.azteca.chatapp.common.Service.Companion.getCurrentUid
import com.azteca.chatapp.data.model.ChatroomModelResponse
import com.azteca.chatapp.data.model.UserModelResponse
import com.azteca.chatapp.databinding.ItemChatHomeBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class HomeChatAdapter(
    options: FirestoreRecyclerOptions<ChatroomModelResponse>,
    private val itemListener: (UserModelResponse) -> Unit
) : FirestoreRecyclerAdapter<ChatroomModelResponse, HomeChatAdapter.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_chat_home, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: ChatroomModelResponse) {

        holder.bind(model, itemListener)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemChatHomeBinding.bind(view)

        @SuppressLint("SetTextI18n")
        fun bind(model: ChatroomModelResponse, itemListener: (UserModelResponse) -> Unit) {
            Service.getOtherUserFromChatRoom(model.listUser).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val useModel = it.result.toObject(UserModelResponse::class.java)
                    binding.itemTvUsername.text = useModel?.username ?: ""
                    binding.itemTvLastMsg.text =
                        if (model.lastMsgSenderId == getCurrentUid()) " Yo: ${model.lastMsg}" else model.lastMsg
                    binding.itemTvTime.text =
                        model.timestamp?.let { it1 -> Service.timeToTime(it1) }


                    Service.refImgProfileUser(
                        useModel?.userId ?: ""
                    ).downloadUrl.addOnCompleteListener { ref ->
                        if (ref.isSuccessful) {
                            val uri = ref.result
                            Glide.with(itemView.context)
                                .load(uri)
                                .apply(RequestOptions.circleCropTransform())
                                .into(binding.itemIvUser)
                        }
                    }

                    itemView.setOnClickListener { useModel?.let { it1 -> itemListener(it1) } }
                }
            }
        }
    }
}