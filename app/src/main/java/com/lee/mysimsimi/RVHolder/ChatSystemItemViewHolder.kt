package com.lee.mysimsimi.RVHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lee.mysimsimi.Interface.IChat
import com.lee.mysimsimi.Model.Chat
import kotlinx.android.synthetic.main.layout_chat_system_item.view.*

class ChatSystemItemViewHolder(
    itemView: View,
    iChat: IChat
) : RecyclerView.ViewHolder(itemView) {

    private val systemMsg = itemView.system_text_item

    private var iChat: IChat? = null

    init {
        this.iChat = iChat
    }

    // 뷰가 묶이기 전에 보낼 데이터
    fun bindWithView(systemMs: Chat) {
        systemMsg.text = systemMs.utext
    }
}
