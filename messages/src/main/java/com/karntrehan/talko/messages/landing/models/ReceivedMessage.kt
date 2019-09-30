package com.karntrehan.talko.messages.landing.models

data class ReceivedMessage(
    val content: String?,
    val avatarUrl: String? = null
)