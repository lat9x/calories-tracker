package com.tuan.core.util

import android.content.Context
import androidx.annotation.StringRes

/**
 * Một cách hay để xử lý xâu trong UI. Thứ nhất là DynamicString, có thể là các xâu mà service từ xa
 * trả về, xâu này không thể dịch được. Thứ hai là StringResource, là xâu đã được định nghĩa
 * trong strings.xml, nhằm đảm bảo việc phiên dịch cho các ngôn ngữ khác nhau.
 */
sealed class UiText {
    data class DynamicString(val text: String) : UiText()
    data class StringResource(@StringRes val resId: Int) : UiText()

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> text
            is StringResource -> context.getString(resId)
        }
    }
}
