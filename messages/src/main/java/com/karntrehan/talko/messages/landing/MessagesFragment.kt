package com.karntrehan.talko.messages.landing

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karntrehan.talko.architecture.BaseFragment
import com.karntrehan.talko.extensions.EndlessScrollListener
import com.karntrehan.talko.messages.R
import com.karntrehan.talko.messages.di.MessagesDH
import kotlinx.android.synthetic.main.messages_fragment.*
import javax.inject.Inject

class MessagesFragment : BaseFragment() {

    override val layout = R.layout.messages_fragment

    override val vmClass = MessagesVM::class.java

    @Inject
    lateinit var messagesVMF: MessagesVMF

    val viewModel: MessagesVM by lazy { baseVM as MessagesVM }

    //Pagination
    private lateinit var endlessScrollListener: EndlessScrollListener

    companion object {
        const val TAG = "MessagesFragment"
        fun newInstance() = MessagesFragment()
    }

    override fun provideViewModelFactory() = messagesVMF

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        endlessScrollListener = initEndlessScroll()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        MessagesDH.messagesComponent.inject(this)
    }

    private fun initEndlessScroll() = object : EndlessScrollListener(
        layoutManager = rvMessages.layoutManager as LinearLayoutManager,
        visibleThreshold = 1
    ) {
        //This will be called each time the user scrolls
        // and only 2 elements are left in the recyclerview items.
        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
            //viewModel.loadNextPage()
        }
    }

    override fun hideLoading() {
        srlMessages.isRefreshing = false
    }

    override fun showLoading() {
        srlMessages.isRefreshing = true
    }

}
