package com.karntrehan.talko.messages.landing.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.karntrehan.talko.extensions.CircleTransform
import com.karntrehan.talko.extensions.inflate
import com.karntrehan.talko.messages.R
import com.karntrehan.talko.messages.landing.models.ReceivedMessage
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_recieved_message.*

class ReceivedMessageAdapterDelegate(val picasso: Picasso) :
    AbsListItemAdapterDelegate<ReceivedMessage, Any, ReceivedMessageAdapterDelegate.ReceivedMessageVH>() {

    val circleTransform = CircleTransform()

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int) =
        items[position] is ReceivedMessage

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
            picasso.load(message.avatarUrl).transform(circleTransform).into(ivAvatar)
        }
    }
}