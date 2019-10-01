package com.karntrehan.talko.messages.landing.models

data class ReceivedName(
    val userId: Int,
    val name: String?
) : MessageModel(userId)