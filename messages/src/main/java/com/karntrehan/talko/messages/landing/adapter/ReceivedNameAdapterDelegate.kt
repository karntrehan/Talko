package com.karntrehan.talko.messages.landing.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.karntrehan.talko.extensions.inflate
import com.karntrehan.talko.messages.R
import com.karntrehan.talko.messages.landing.models.ReceivedName
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_recieved_username.*

class ReceivedNameAdapterDelegate() :
    AbsListItemAdapterDelegate<ReceivedName, Any, ReceivedNameAdapterDelegate.ReceivedMessageVH>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int) =
        items[position] is ReceivedName

    override fun onCreateViewHolder(parent: ViewGroup) =
        ReceivedMessageVH(parent.inflate(R.layout.item_recieved_username))

    override fun onBindViewHolder(
        item: ReceivedName,
        holder: ReceivedMessageVH,
        payloads: List<Any>
    ) = holder.bind(item)

    inner class ReceivedMessageVH(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(user: ReceivedName) {
            tvUsername.text = user.name
        }
    }
}