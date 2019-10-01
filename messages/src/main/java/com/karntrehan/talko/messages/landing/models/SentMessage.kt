package com.karntrehan.talko.messages.landing.models

data class SentMessage(
    val messageId: Int,
    val content: String?
) : MessageModel(messageId)