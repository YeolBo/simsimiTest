package com.lee.mysimsimi.RVAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lee.mysimsimi.Interface.IChat
import com.lee.mysimsimi.Model.Chat
import com.lee.mysimsimi.R
import com.lee.mysimsimi.RVHolder.ChatBotItemViewHolder
import com.lee.mysimsimi.RVHolder.ChatSystemItemViewHolder
import com.lee.mysimsimi.RVHolder.ChatUserItemViewHolder

// RecyclerViewAdapter 에 내가 만든 Holder 를 장착
class ChatRecyclerViewAdapter(val iChat: IChat) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 리사이클러뷰에서 보여줄 아이템을 담을 리스트
    private var chatList = ArrayList<Chat>()

    // 메세지 타입 - 나, 봇, 시스템
    // value: Int 로 구분한다.
    enum class MsgType(val value: Int) {
        ME(0),
        BOT(1),
        SYSTEM(2)
    }

    // 뷰 홀더와 레이아웃 연결, 생성되는 부분
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        // user 메세지 홀더
        val myMsgViewHolder = ChatUserItemViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.layout_chat_user_item, parent, false),
            iChat
        )

        // bot 메세지 홀더
        val botMsgViewHolder = ChatBotItemViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.layout_chat_bot_item, parent, false),
            iChat
        )

        // system 메세지 홀더
        val systemMsgViewHolder = ChatSystemItemViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.layout_chat_system_item, parent, false),
            iChat
        )

        return when (viewType) {
            MsgType.ME.value -> myMsgViewHolder
            MsgType.BOT.value -> botMsgViewHolder
            else -> systemMsgViewHolder
        }
    }

    // 뷰가 묶였을때 데이터를 뷰홀더에 넘겨준다
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder.itemViewType) {
            MsgType.ME.value -> {
                (holder as ChatUserItemViewHolder).bindWithView(this.chatList[position])
            }
            MsgType.BOT.value -> {
                (holder as ChatBotItemViewHolder).bindWithView(this.chatList[position])
            }
            else -> {
                (holder as ChatSystemItemViewHolder).bindWithView(this.chatList[position])
            }
        }
    }

    // 보여줄 채팅 수
    override fun getItemCount(): Int {
        return this.chatList.size
    }

    // 각 아이템에 대한 뷰 타입 정하기
    override fun getItemViewType(position: Int): Int {
        val currentChat = chatList[position]

        // 현재 메세지의 타입에 따라 뷰타입의 Int 설정
        return when (currentChat.msgType) {
            MsgType.ME -> MsgType.ME.value
            MsgType.BOT -> MsgType.BOT.value
            else -> MsgType.SYSTEM.value
        }
    }

    // 외부에서 어답터에 데이터 배열을 넣어준다.
    fun submitList(chatList: ArrayList<Chat>) {
        this.chatList = chatList
    }
}