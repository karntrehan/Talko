package com.karntrehan.talko.messages.landing.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.karntrehan.talko.messages.landing.models.*
import com.squareup.picasso.Picasso

class MessagesAdapter(picasso: Picasso, interaction: MessagesInteraction) :
    AsyncListDifferDelegationAdapter<MessageModel>(ItemDC()) {

    init {
        delegatesManager.apply {
            addDelegate(ReceivedMessageAdapterDelegate(picasso, interaction))
            addDelegate(ReceivedNameAdapterDelegate())
            addDelegate(ReceivedAttachmentAdapterDelegate(picasso, interaction))

            addDelegate(SentMessageAdapterDelegate(interaction))
            addDelegate(SentNameAdapterDelegate())
            addDelegate(SentAttachmentAdapterDelegate(picasso, interaction))
        }
        setHasStableIds(true)
    }

    override fun getItemId(position: Int) = items[position].longId()

    interface MessagesInteraction {

        fun attachmentDeleteTriggered(
            adapterPosition: Int,
            attachment: SentAttachment
        )

        fun attachmentDeleteTriggered(
            adapterPosition: Int,
            attachment: ReceivedAttachment
        )

        fun messageDeleteTriggered(adapterPosition: Int, message: ReceivedMessage)

        fun messageDeleteTriggered(adapterPosition: Int, message: SentMessage)
    }

    private class ItemDC : DiffUtil.ItemCallback<MessageModel>() {
        override fun areItemsTheSame(
            oldItem: MessageModel,
            newItem: MessageModel
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: MessageModel,
            newItem: MessageModel
        ) = if (oldItem is ReceivedMessage && newItem is ReceivedMessage)
            oldItem as ReceivedMessage == newItem as ReceivedMessage
        else if (oldItem is ReceivedName && newItem is ReceivedName)
            oldItem as ReceivedName == newItem as ReceivedName
        else if (oldItem is SentName && newItem is SentName)
            oldItem as SentName == newItem as SentName
        else if (oldItem is SentMessage && newItem is SentMessage)
            oldItem as SentMessage == newItem as SentMessage
        else if (oldItem is ReceivedAttachment && newItem is ReceivedAttachment)
            oldItem as ReceivedAttachment == newItem as ReceivedAttachment
        else if (oldItem is SentAttachment && newItem is SentAttachment)
            oldItem as SentAttachment == newItem as SentAttachment
        else false
    }

}