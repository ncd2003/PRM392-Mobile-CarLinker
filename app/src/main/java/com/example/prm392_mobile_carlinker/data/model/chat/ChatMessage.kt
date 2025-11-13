package com.example.prm392_mobile_carlinker.data.model.chat

import com.google.gson.annotations.SerializedName

/**
 * Chat Message Response Model
 * Represents a single message in a chat room
 */
data class ChatMessage(
    @SerializedName("id")
    val id: Long,

    @SerializedName("roomId")
    val roomId: Long,

    @SerializedName("senderType")
    val senderType: Int, // 0=CUSTOMER, 1=STAFF, 2=ADMIN

    @SerializedName("senderId")
    val senderId: Int,

    @SerializedName("senderName")
    val senderName: String?,

    @SerializedName("message")
    val message: String?,

    @SerializedName("messageType")
    val messageType: Int, // 0=TEXT, 1=MEDIA, 2=SYSTEM

    @SerializedName("fileUrl")
    val fileUrl: String?,

    @SerializedName("fileType")
    val fileType: Int?, // 0=IMAGE, 1=VIDEO, 2=FILE

    @SerializedName("status")
    val status: Int, // 0=ACTIVE, 1=EDITED, 2=HIDDEN

    @SerializedName("isRead")
    val isRead: Boolean,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("updatedAt")
    val updatedAt: String?
) {
    fun getSenderTypeEnum(): SenderType = SenderType.fromValue(senderType)
    fun getMessageTypeEnum(): MessageType = MessageType.fromValue(messageType)
    fun getMessageStatusEnum(): MessageStatus = MessageStatus.fromValue(status)
    fun getFileTypeEnum(): FileType? = fileType?.let { FileType.fromValue(it) }
}

/**
 * Send Message Request Model
 */
data class SendMessageRequest(
    @SerializedName("roomId")
    val roomId: Long,

    @SerializedName("senderType")
    val senderType: Int,

    @SerializedName("senderId")
    val senderId: Int,

    @SerializedName("message")
    val message: String?,

    @SerializedName("messageType")
    val messageType: Int,

    @SerializedName("fileUrl")
    val fileUrl: String? = null,

    @SerializedName("fileType")
    val fileType: Int? = null
)

/**
 * Upload File Response Model
 */
data class UploadFileResponse(
    @SerializedName("fileUrl")
    val fileUrl: String,

    @SerializedName("fileName")
    val fileName: String,

    @SerializedName("fileType")
    val fileType: Int,

    @SerializedName("fileSize")
    val fileSize: Long,

    @SerializedName("uploadedAt")
    val uploadedAt: String
)

/**
 * Edit Message Request Model - UC-03
 */
data class EditMessageRequest(
    @SerializedName("message")
    val message: String,

    @SerializedName("senderId")
    val senderId: Int,

    @SerializedName("senderType")
    val senderType: Int
)

/**
 * Hide Message Request Model - UC-03
 */
data class HideMessageRequest(
    @SerializedName("senderId")
    val senderId: Int,

    @SerializedName("senderType")
    val senderType: Int
)

/**
 * Hide Message Response Model - UC-03
 */
data class HideMessageResponse(
    @SerializedName("messageId")
    val messageId: Long,

    @SerializedName("roomId")
    val roomId: Long,

    @SerializedName("hiddenAt")
    val hiddenAt: String
)
