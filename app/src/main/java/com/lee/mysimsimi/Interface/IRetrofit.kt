package com.lee.mysimsimi.Interface

import com.lee.mysimsimi.Model.Chat
import com.lee.mysimsimi.Model.MsgResponse
import com.lee.mysimsimi.Utils.API
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface IRetrofit {
    // POST 로 받는다
    @POST(API.BASE_URL)
    fun sendMsg(@Body chat: Chat): Call<MsgResponse>
}