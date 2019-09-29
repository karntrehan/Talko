package com.karntrehan.talko.messages.landing.models

import com.google.gson.annotations.SerializedName
import com.karntrehan.talko.messages.db.tables.Message
import com.karntrehan.talko.messages.db.tables.User

data class MessagesAndUsersJsonModel(
    @SerializedName("messages") val messages: List<Message>,
    @SerializedName("users") val users: List<User>
)