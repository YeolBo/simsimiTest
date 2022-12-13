package com.lee.mysimsimi.Utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

// 문자열이 json 형태인지
fun String?.isJsonObject(): Boolean {
    return this?.startsWith("{") == true && this.endsWith("}")
}

// 문자열이 json 배열인지
fun String?.isJsonArray(): Boolean {
    return this?.startsWith("[") == true && this.endsWith("]")
}

// 에딧 텍스트에 대한 익스텐션
fun EditText.onMyText(completion: (Editable?) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            completion(editable)
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}