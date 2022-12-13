package com.lee.mysimsimi.Retrofit

import android.util.Log
import com.lee.mysimsimi.Utils.API
import com.lee.mysimsimi.Utils.Constants.TAG
import com.lee.mysimsimi.Utils.isJsonArray
import com.lee.mysimsimi.Utils.isJsonObject
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    // 레트로핏 클라이언트 선언
    private var retrofitClient: Retrofit? = null

    // 레트로핏 클라이언트 가져오기
    fun getClient(baseUrl: String): Retrofit? {
        // okhttp 인스턴스 생성
        val client = OkHttpClient.Builder()

        // 로그를 찍기 위해 로깅 인터셉터를 추가
        val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d(TAG, "RetrofitClient / 로그를 찍기 위해 로깅 인터셉터를 추가 \nmessage : $message")

                when {
                    message.isJsonObject() -> Log.d(TAG, JSONObject(message).toString(4))
                    message.isJsonArray() -> Log.d(TAG, JSONArray(message).toString(4))
                    else -> {
                        try {
                            Log.d(TAG, JSONObject(message).toString(4))
                        } catch (e: Exception) {
                            Log.d(TAG, message)
                        }
                    }
                }
            }
        })
        // 로깅 인터셉터의 레벨을 body 로 해준다.
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        // 위에서 설정한 로깅 인터셉터를 okhttp 클라이언트에 추가 한다.
        client.addInterceptor((loggingInterceptor))

        // 기본 파라메터 인터셉터 설정
        val baseParameterInterceptor: Interceptor = (object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                Log.d(TAG, "RetrofitClient - intercept() called / 기본 파라메터 인터셉터 설정")

                // 오리지날 리퀘스터
                val originalRequest = chain.request()

                // api 키를 헤더에 넣기위해 만들어준다.
                val apiKeyAdded = originalRequest.newBuilder()
                    // 헤더네임을 같게 해줘야한다.
                    .addHeader("x-api-key", API.X_API_KEY).build()

                val finalRequest = apiKeyAdded.newBuilder()
                    .method(originalRequest.method, originalRequest.body).build()
                return chain.proceed(finalRequest)
            }
        })

        // 위에서 설정한 기본 파라메터 인터셉터를 okhttp 클라이언트에 추가 한다.
        client.addInterceptor(baseParameterInterceptor)

        // 커넥션 타임아웃
        client.connectTimeout(10, TimeUnit.SECONDS)
        client.readTimeout(10, TimeUnit.SECONDS)
        client.writeTimeout(10, TimeUnit.SECONDS)
        client.retryOnConnectionFailure(true)

        // 선언한 retrofitClient 가 비어있으면
        if (retrofitClient == null) {
            // 레트로핏 빌더를 통해 인스턴스 생성 후
            retrofitClient = Retrofit.Builder()
                // baseUrl 은 URL 중 변하지 않는 도메인 주소를 뜻한다.
                .baseUrl(baseUrl)
                // 서버로부터 데이터를 받아와서 원하는 타입으로 데이터를 바꾸기 위해
                // addConverterFactory(GsonConverterFactory.create())를 사용한다.
                .addConverterFactory(GsonConverterFactory.create())
                // 위에서 설정한 클라이언트로 레트로핏 클라이언트를 설정한다.
                .client(client.build()).build()
        }
        return retrofitClient
    }
}
