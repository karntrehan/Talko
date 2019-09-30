package com.karntrehan.talko.messages.landing

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karntrehan.talko.architecture.BaseFragment
import com.karntrehan.talko.extensions.EndlessScrollListener
import com.karntrehan.talko.messages.R
import com.karntrehan.talko.messages.di.MessagesDH
import com.karntrehan.talko.messages.landing.adapter.MessagesAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.messages_fragment.*
import javax.inject.Inject

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

        observeMessages()

        viewModel.messages()

        rvMessages.adapter = adapter

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

}
