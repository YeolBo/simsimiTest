package com.lee.mysimsimi.Model

import com.lee.mysimsimi.RVAdapter.ChatRecyclerViewAdapter
import kotlinx.serialization.Serializable

// 입력받은 문자를 담을 utext
// 심심이 api 에서 요구하는 문자코드 "ko"
@Serializable
data class Chat(
    var utext: String,
    var lang: String = "ko",
    // 메세지 타입 추가 옵셔널로 해야함
    var msgType: ChatRecyclerViewAdapter.MsgType? = ChatRecyclerViewAdapter.MsgType.ME,
)

@Serializable
data class MsgResponse(
    val status: Long? = null,
    val statusMessage: String? = null,
    val request: Chat? = null,
    val atext: String? = null,
    val lang: String? = null,
    val systemMsg: String = "대화 종료"
)

// 익스텐션을 활용한 예
fun MsgResponse.toChat(msgType: ChatRecyclerViewAdapter.MsgType): Chat {
//    return Chat(this.atext ?: "", msgType = msgType)
    return when (msgType) {
        ChatRecyclerViewAdapter.MsgType.BOT -> Chat(this.atext ?: "", msgType = msgType)
        else -> Chat(this.systemMsg, msgType = msgType)
    }
}