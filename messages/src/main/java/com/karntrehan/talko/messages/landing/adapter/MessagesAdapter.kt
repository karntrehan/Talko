package com.karntrehan.talko.messages.landing.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.karntrehan.talko.messages.landing.models.*
import com.squareup.picasso.Picasso

class MessagesAdapter(picasso: Picasso, interaction: MessagesInteraction) :
    AsyncListDifferDelegationAdapter<Any>(ItemDC()) {

    init {
        delegatesManager.apply {

            //Add new delegates here
            addDelegate(ReceivedMessageAdapterDelegate(picasso))
            addDelegate(ReceivedNameAdapterDelegate())
            addDelegate(ReceivedAttachmentAdapterDelegate(picasso))

            addDelegate(SentMessageAdapterDelegate())
            addDelegate(SentNameAdapterDelegate())
            addDelegate(SentAttachmentAdapterDelegate(picasso))

            //fallbackDelegate = HomeFallbackAdapter()
        }
    }

    interface MessagesInteraction {

    }

    private class ItemDC : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(
            oldItem: Any,
            newItem: Any
        ) = if (oldItem is ReceivedMessage && newItem is ReceivedMessage)
            oldItem.avatarUrl == newItem.avatarUrl
        else if (oldItem is ReceivedName && newItem is ReceivedName)
            oldItem.name == newItem.name
        else if (oldItem is SentMessage && newItem is SentMessage)
            oldItem.content == newItem.content
        else if (oldItem is ReceivedAttachment && newItem is ReceivedAttachment)
            oldItem.thumbnailUrl == newItem.thumbnailUrl
        else if (oldItem is SentAttachment && newItem is SentAttachment)
            oldItem.thumbnailUrl == newItem.thumbnailUrl
        else false

        override fun areContentsTheSame(
            oldItem: Any,
            newItem: Any
        ) = if (oldItem is ReceivedMessage && newItem is ReceivedMessage)
            oldItem as ReceivedMessage == newItem as ReceivedMessage
        else if (oldItem is ReceivedName && newItem is ReceivedName)
            oldItem as ReceivedName == newItem as ReceivedName
        else if (oldItem is SentMessage && newItem is SentMessage)
            oldItem as SentMessage == newItem as SentMessage
        else if (oldItem is ReceivedAttachment && newItem is ReceivedAttachment)
            oldItem as ReceivedAttachment == newItem as ReceivedAttachment
        else if (oldItem is SentAttachment && newItem is SentAttachment)
            oldItem as SentAttachment == newItem as SentAttachment
        else false
    }

}