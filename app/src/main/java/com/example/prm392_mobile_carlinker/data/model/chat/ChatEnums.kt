package com.example.prm392_mobile_carlinker.data.model.chat

/**
 * Enum for sender type in chat
 */
enum class SenderType(val value: Int) {
    CUSTOMER(0),
    STAFF(1),
    ADMIN(2);

    companion object {
        fun fromValue(value: Int): SenderType {
            return values().find { it.value == value } ?: CUSTOMER
        }
    }
}

/**
 * Enum for message type in chat
 */
enum class MessageType(val value: Int) {
    TEXT(0),
    MEDIA(1),
    SYSTEM(2);

    companion object {
        fun fromValue(value: Int): MessageType {
            return values().find { it.value == value } ?: TEXT
        }
    }
}

/**
 * Enum for message status
 */
enum class MessageStatus(val value: Int) {
    ACTIVE(0),
    EDITED(1),
    HIDDEN(2);

    companion object {
        fun fromValue(value: Int): MessageStatus {
            return values().find { it.value == value } ?: ACTIVE
        }
    }
}

/**
 * Enum for file type in media messages
 */
enum class FileType(val value: Int) {
    IMAGE(0),
    VIDEO(1),
    FILE(2);

    companion object {
        fun fromValue(value: Int): FileType {
            return values().find { it.value == value } ?: IMAGE
        }
    }
}
