package com.karntrehan.talko.messages.landing

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karntrehan.talko.architecture.BaseFragment
import com.karntrehan.talko.utils.BottomMarginDecoration
import com.karntrehan.talko.utils.EndlessScrollListener
import com.karntrehan.talko.messages.R
import com.karntrehan.talko.messages.di.MessagesDH
import com.karntrehan.talko.messages.landing.adapter.MessagesAdapter
import com.karntrehan.talko.messages.landing.models.ReceivedAttachment
import com.karntrehan.talko.messages.landing.models.SentAttachment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.messages_fragment.*
import javax.inject.Inject
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import com.karntrehan.talko.messages.landing.models.ReceivedMessage
import com.karntrehan.talko.messages.landing.models.SentMessage


class MessagesFragment : BaseFragment(), MessagesAdapter.MessagesInteraction {

    override val layout = R.layout.messages_fragment

    override val vmClass = MessagesVM::class.java

    @Inject
    lateinit var messagesVMF: MessagesVMF
    @Inject
    lateinit var picasso: Picasso

    private val viewModel: MessagesVM by lazy { baseVM as MessagesVM }

    private val adapter: MessagesAdapter by lazy { MessagesAdapter(picasso, this) }

    //Pagination
    private lateinit var endlessScrollListener: EndlessScrollListener

    companion object {
        const val TAG = "MessagesFragment"
        fun newInstance() = MessagesFragment()
    }

    override fun provideViewModelFactory() = messagesVMF

    override fun onAttach(context: Context) {
        super.onAttach(context)
        MessagesDH.messagesComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        srlMessages.isEnabled = false
        rvMessages.addItemDecoration(BottomMarginDecoration())
        rvMessages.adapter = adapter

        observeMessages()
        viewModel.messages()

        endlessScrollListener = initEndlessScroll()
    }

    private fun observeMessages() {
        viewModel.messages.observe(viewLifecycleOwner, Observer { messages ->
            adapter.items = messages
        })
    }

    override fun onResume() {
        super.onResume()
        rvMessages.addOnScrollListener(endlessScrollListener)
    }

    private fun initEndlessScroll() = object : EndlessScrollListener(
        layoutManager = rvMessages.layoutManager as LinearLayoutManager,
        visibleThreshold = 2
    ) {
        //This will be called each time the user scrolls
        // and only 2 elements are left in the recyclerview items.
        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
            viewModel.loadNextMessages()
        }
    }

    override fun hideLoading() {
        srlMessages.isRefreshing = false
    }

    override fun showLoading() {
        srlMessages.isRefreshing = true
    }

    override fun attachmentDeleteTriggered(adapterPosition: Int, attachment: SentAttachment) {
        attachmentDeleteTriggered(adapterPosition, attachment.id)
    }

    override fun attachmentDeleteTriggered(adapterPosition: Int, attachment: ReceivedAttachment) {
        attachmentDeleteTriggered(adapterPosition, attachment.id)
    }

    private fun attachmentDeleteTriggered(position: Int, id: String) {
        confirmDeletion(DialogInterface.OnClickListener { dialog, _ ->
            dialog.dismiss()
            viewModel.deleteAttachment(position, id)
        })
    }

    override fun messageDeleteTriggered(adapterPosition: Int, message: ReceivedMessage) {
        messageDeleteTriggered(adapterPosition, message.receivedMessageId)
    }

    override fun messageDeleteTriggered(adapterPosition: Int, message: SentMessage) {
        messageDeleteTriggered(adapterPosition, message.messageId)
    }

    private fun messageDeleteTriggered(position: Int, id: Int) {
        confirmDeletion(DialogInterface.OnClickListener { dialog, _ ->
            dialog.dismiss()
            viewModel.deleteMessage(position, id)
        })
    }

    private fun confirmDeletion(listener: DialogInterface.OnClickListener) {
        context?.let {
            AlertDialog.Builder(ContextThemeWrapper(it, com.karntrehan.talko.R.style.AlertDialog))
                .setTitle(getString(R.string.delete_entry))
                .setPositiveButton(R.string.delete, listener)
                .setNegativeButton(android.R.string.no, null)
                .show()
        }

    }

}
