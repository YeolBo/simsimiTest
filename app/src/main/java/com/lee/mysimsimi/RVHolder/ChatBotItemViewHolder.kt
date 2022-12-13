package com.lee.mysimsimi.RVHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lee.mysimsimi.Interface.IChat
import com.lee.mysimsimi.Model.Chat
import kotlinx.android.synthetic.main.layout_chat_bot_item.view.*

class ChatBotItemViewHolder(
    itemView: View,
    iChat: IChat
) : RecyclerView.ViewHolder(itemView) {

    private val botMsg = itemView.bot_text_item

    private var iChat: IChat? = null

    init {
        this.iChat = iChat
    }

    // 뷰가 묶이기 전에 보낼 데이터
    fun bindWithView(userItem: Chat) {
        botMsg.text = userItem.utext
    }
}
