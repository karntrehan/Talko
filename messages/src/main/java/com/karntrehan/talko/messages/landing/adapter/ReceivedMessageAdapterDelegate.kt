package com.karntrehan.talko.messages.landing.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.karntrehan.talko.extensions.*
import com.karntrehan.talko.utils.CircleTransform
import com.karntrehan.talko.messages.R
import com.karntrehan.talko.messages.landing.models.MessageModel
import com.karntrehan.talko.messages.landing.models.ReceivedMessage
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_recieved_message.*

class ReceivedMessageAdapterDelegate(
    val picasso: Picasso,
    val interaction: MessagesAdapter.MessagesInteraction
) : AbsListItemAdapterDelegate<ReceivedMessage, MessageModel, ReceivedMessageAdapterDelegate.ReceivedMessageVH>() {

    val circleTransform = CircleTransform()

    override fun isForViewType(
        item: MessageModel,
        items: MutableList<MessageModel>,
        position: Int
    ) = items[position] is ReceivedMessage

    override fun onCreateViewHolder(parent: ViewGroup) =
        ReceivedMessageVH(parent.inflate(R.layout.item_recieved_message))

    override fun onBindViewHolder(
        item: ReceivedMessage,
        holder: ReceivedMessageVH,
        payloads: List<Any>
    ) = holder.bind(item)

    inner class ReceivedMessageVH(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(message: ReceivedMessage) {
            tvMessage.text = message.content
            if (message.avatarUrl.isValid()) {
                ivAvatar.visible()
                picasso.load(message.avatarUrl)
                    .placeholder(randomColor())
                    .transform(circleTransform)
                    .into(ivAvatar)
            } else ivAvatar.invisible()
            itemView.setOnLongClickListener {
                interaction.messageDeleteTriggered(adapterPosition, message)
                return@setOnLongClickListener true
            }
        }
    }
}