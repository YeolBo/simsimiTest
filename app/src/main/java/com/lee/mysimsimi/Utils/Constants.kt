package com.lee.mysimsimi.Utils

object Constants {
    const val TAG: String = "로그"
}

// api 호출상태를 위한 이넘 클래스
enum class RESPONSE_STATUS {
    OKAY,
    FAIL
}

// 싱글턴
object API {
    const val BASE_URL: String = "https://wsapi.simsimi.com/190410/talk/"
    const val X_API_KEY: String = "엫헿"
}