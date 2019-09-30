package com.karntrehan.talko.messages.landing.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.karntrehan.talko.extensions.inflate
import com.karntrehan.talko.messages.R
import com.karntrehan.talko.messages.landing.models.SentName
import kotlinx.android.extensions.LayoutContainer

class SentNameAdapterDelegate() :
    AbsListItemAdapterDelegate<SentName, Any, SentNameAdapterDelegate.ReceivedMessageVH>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int) =
        items[position] is SentName

    override fun onCreateViewHolder(parent: ViewGroup) =
        ReceivedMessageVH(parent.inflate(R.layout.item_sent_username))

    override fun onBindViewHolder(
        item: SentName,
        holder: ReceivedMessageVH,
        payloads: List<Any>
    ) = holder.bind()

    inner class ReceivedMessageVH(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind() {}
    }
}