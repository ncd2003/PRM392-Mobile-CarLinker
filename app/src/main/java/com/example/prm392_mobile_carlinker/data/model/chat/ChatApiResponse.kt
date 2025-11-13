package com.example.prm392_mobile_carlinker.data.model.chat

import com.google.gson.annotations.SerializedName

/**
 * Generic API Response Wrapper for Chat APIs
 */
data class ChatApiResponse<T>(
    @SerializedName("status")
    val status: Int,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: T?
)
