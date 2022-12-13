package com.lee.mysimsimi

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.lee.mysimsimi.Model.Chat
import com.lee.mysimsimi.Model.toChat
import com.lee.mysimsimi.RVAdapter.ChatRecyclerViewAdapter
import com.lee.mysimsimi.RVAdapter.ChatRecyclerViewAdapter.MsgType
import com.lee.mysimsimi.Interface.IChat
import com.lee.mysimsimi.Retrofit.RetrofitManager
import com.lee.mysimsimi.Utils.Constants.TAG
import com.lee.mysimsimi.Utils.RESPONSE_STATUS
import com.lee.mysimsimi.Utils.onMyText
import kotlinx.android.synthetic.main.activity_chat_room.*
import kotlinx.android.synthetic.main.layout_chat_system_item.*

class ChatRoomActivity : AppCompatActivity(), IChat {
    // 어답터로 넘겨줄 데이터를 담을 리스트
    private var chatList = ArrayList<Chat>()

    // 어답터 생성
    private lateinit var chatGridRecyclerViewAdapter: ChatRecyclerViewAdapter

    @SuppressLint("NotifyDataSetChanged")
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        // 리사이클러뷰 설정
        this.chatGridRecyclerViewAdapter = ChatRecyclerViewAdapter(this)

        // 어답터에 리스트를 보내준다.
        this.chatGridRecyclerViewAdapter.submitList(chatList)

        // 리사이클러뷰 레이아웃 설정
        my_chat_recycler_view.layoutManager = GridLayoutManager(
            this,
            1,
            GridLayoutManager.VERTICAL,
            false
        )
        // 위에서 설정한 어답터를 장착
        my_chat_recycler_view.adapter = this.chatGridRecyclerViewAdapter

        chat_edit_text.onMyText {
            // 입력된 글자가 하나라도 있으면
            if (it.toString().isNotEmpty()) {
                // 스크롤뷰를 올린다
                my_chat_recycler_view.scrollTo(0, 200)
            }
        }

        // 입력 버튼 클릭 시
        text_input_btn.setOnClickListener {
            // 내가 적은 메세지를 먼저 출력해준다.
            val userInput = chat_edit_text.text.toString()
            chatList.add(Chat(utext = userInput))

            // 리사이클러뷰 어답터에게 데이터가 추가되었다고 알린다.
            chatGridRecyclerViewAdapter.notifyDataSetChanged()

            // 검색 api 호출
            RetrofitManager.instance.simsimChat(
                simsimTerm = chat_edit_text.text.toString(),
                completion = { responseStatus, responseChatMsg ->
                    when (responseStatus) {
                        RESPONSE_STATUS.OKAY -> {
                            Log.d(TAG, "api 호출 성공 : $responseChatMsg")

                            // 봇에 메세지를 받아서 기존 리스트에 추가한다.
                            val newBotMsg =
                                responseChatMsg?.toChat(MsgType.BOT) ?: return@simsimChat

                            Log.d(TAG, "$newBotMsg")

                            // 기다렸다가 봇 응답이 들어오면 들어온 메세지를 어답터에 연결되어 있는 리스트에 추가한다.
                            chatList.add(newBotMsg)

                            // system 메세지
                            val systemMsg = responseChatMsg.toChat(MsgType.SYSTEM)
                            chatList.add(systemMsg)

                            // 리사이클러뷰 어답터에게 데이터가 추가되었다고 알린다.
                            chatGridRecyclerViewAdapter.notifyDataSetChanged()
                        }
                        RESPONSE_STATUS.FAIL -> {
                            Log.d(TAG, "api 호출 실패 : $responseChatMsg")
                        }
                    }
                })
            // 버튼 클릭시 에딧 텍스트 초기화
            chat_edit_text.setText("")
        }
    }
}
