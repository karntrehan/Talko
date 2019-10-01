package com.karntrehan.talko.messages.landing.models

data class ReceivedAttachment(
    val receivedAttachmentId: String,
    val thumbnailUrl: String?,
    val title: String?
) : MessageModel(receivedAttachmentId)
