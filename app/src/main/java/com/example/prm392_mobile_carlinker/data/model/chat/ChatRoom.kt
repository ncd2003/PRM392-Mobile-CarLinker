package com.example.prm392_mobile_carlinker.data.model.chat

package com.example.prm392_mobile_carlinker.data.model.chat

import com.google.gson.annotations.SerializedName

/**
 * Chat Room Response Model
 * Represents a chat room between a customer and a garage
 */
data class ChatRoom(
    @SerializedName("id")
    val id: Long,

    @SerializedName("garageId")
    val garageId: Int,

    @SerializedName("garageName")
    val garageName: String?,

    @SerializedName("customerId")
    val customerId: Int,

    @SerializedName("customerName")
    val customerName: String?,

    @SerializedName("lastMessageAt")
    val lastMessageAt: String?,

    @SerializedName("lastMessage")
    val lastMessage: ChatMessage?,

    @SerializedName("createdAt")
    val createdAt: String
)

/**
 * Create or Get Chat Room Request Model
 */
data class CreateChatRoomRequest(
    @SerializedName("garageId")
    val garageId: Int,

    @SerializedName("customerId")
    val customerId: Int
)
