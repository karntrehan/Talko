package com.karntrehan.talko.messages.landing.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class MessagesAdapter(interaction: MessagesInteraction) :
    AsyncListDifferDelegationAdapter<Any>(ItemDC()) {

    init {
        delegatesManager.apply {

            //Add new delegates here
            addDelegate(ReceivedMessageAdapterDelegate())
            addDelegate(ReceivedNameAdapterDelegate())
            addDelegate(SentMessageAdapterDelegate())
            addDelegate(SentNameAdapterDelegate())

            //fallbackDelegate = HomeFallbackAdapter()
        }
    }

    interface MessagesInteraction {

    }

    private class ItemDC : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(
            oldItem: Any,
            newItem: Any
        ) = false

        override fun areContentsTheSame(
            oldItem: Any,
            newItem: Any
        ) = false
    }

}