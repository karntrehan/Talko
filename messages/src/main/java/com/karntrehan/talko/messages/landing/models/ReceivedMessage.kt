package com.karntrehan.talko.messages.landing.models

data class ReceivedMessage(
    val receivedMessageId: Int,
    val content: String?,
    val avatarUrl: String? = null
) : MessageModel(receivedMessageId)