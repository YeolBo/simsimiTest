package com.lee.mysimsimi.Retrofit

import android.annotation.SuppressLint
import android.util.Log
import com.lee.mysimsimi.Interface.IRetrofit
import com.lee.mysimsimi.Model.Chat
import com.lee.mysimsimi.Model.MsgResponse
import com.lee.mysimsimi.Utils.API
import com.lee.mysimsimi.Utils.RESPONSE_STATUS
import com.lee.mysimsimi.Utils.Constants.TAG
import retrofit2.Call
import retrofit2.Response

class RetrofitManager {

    // instance 에 RetrofitManager 을 담아준다.
    companion object {
        val instance = RetrofitManager()
    }

    // 레트로핏 인터페이스를 가져오면서 http 콜 만들기
    private val iRetrofit: IRetrofit? =
        RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)

    // 심심이 답장 API 호출
    // 호출 상태와 api 로 부터 받은 MsgResponse
    fun simsimChat(simsimTerm: String?, completion: (RESPONSE_STATUS, MsgResponse?) -> Unit) {
        // 언랩핑
        val term = simsimTerm ?: return
        val requestMsg = Chat(term)

        val call = iRetrofit?.sendMsg(requestMsg) ?: return

        call.enqueue(object : retrofit2.Callback<MsgResponse> {
            // 응답 실패시 RESPONSE_STATUS.FAIL 로 보내준다.
            override fun onFailure(call: Call<MsgResponse>, t: Throwable) {
                Log.d(TAG, "응답 실패 - onFailure() called / t : $t")
                completion(RESPONSE_STATUS.FAIL, null)
            }

            // 응답 성공시 api 상태를 RESPONSE_STATUS.OKAY 로 보내고 response.body()를 보여준다.
            @SuppressLint("SimpleDateFormat")
            override fun onResponse(call: Call<MsgResponse>, response: Response<MsgResponse>) {
                Log.d(TAG, "응답 성공 - onResponse() called / response : ${response.raw()}")

                when (response.code()) {
                    // response.code 의 상태코드가 200이면
                    200 -> {
                        completion(RESPONSE_STATUS.OKAY, response.body())
                    }
                }
            }
        })
    }
}