package com.karntrehan.talko.messages.landing.models

data class SentAttachment(
    val sentAttachmentId: String,
    val thumbnailUrl: String?, val title: String?
) : MessageModel(sentAttachmentId)
