package com.karntrehan.talko.messages.landing.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.karntrehan.talko.extensions.inflate
import com.karntrehan.talko.messages.R
import com.karntrehan.talko.messages.landing.models.SentMessage
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_sent_message.*

class SentMessageAdapterDelegate() :
    AbsListItemAdapterDelegate<SentMessage, Any, SentMessageAdapterDelegate.ReceivedMessageVH>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int) =
        items[position] is SentMessage

    override fun onCreateViewHolder(parent: ViewGroup) =
        ReceivedMessageVH(parent.inflate(R.layout.item_sent_message))

    override fun onBindViewHolder(
        item: SentMessage,
        holder: ReceivedMessageVH,
        payloads: List<Any>
    ) = holder.bind(item)

    inner class ReceivedMessageVH(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(message: SentMessage) {
            tvMessage.text = message.content
        }
    }
}