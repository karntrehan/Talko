package com.karntrehan.talko.messages.landing.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.karntrehan.talko.extensions.inflate
import com.karntrehan.talko.messages.R
import com.karntrehan.talko.messages.landing.models.ReceivedAttachment
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_recieved_image.*

class ReceivedAttachmentAdapterDelegate(val picasso: Picasso) :
    AbsListItemAdapterDelegate<ReceivedAttachment, Any, ReceivedAttachmentAdapterDelegate.ReceivedMessageVH>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int) =
        items[position] is ReceivedAttachment

    override fun onCreateViewHolder(parent: ViewGroup) =
        ReceivedMessageVH(parent.inflate(R.layout.item_recieved_image))

    override fun onBindViewHolder(
        item: ReceivedAttachment,
        holder: ReceivedMessageVH,
        payloads: List<Any>
    ) = holder.bind(item)

    inner class ReceivedMessageVH(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(attachment: ReceivedAttachment) {
            tvImageName.text = attachment.title
            picasso.load(attachment.thumbnailUrl).into(ivImage)
        }
    }
}