package com.karntrehan.talko.messages.landing

import com.karntrehan.talko.architecture.BaseVM
import com.karntrehan.talko.extensions.hide
import com.karntrehan.talko.extensions.show
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class MessagesVM(private val repo: MessagesContract.Repo) : BaseVM() {

    private val LIMIT = 20
    private var offset = 0

    val TAG = "MessagesVM"

    fun messages() {
        _loading.show()
        if (repo.isFirstLoad()) {
            repo.loadMessagesIntoMemory(disposable)
                .subscribe {
                    if (it)
                        getLocalMessages()
                }
                .addTo(disposable)
            return
        }

        getLocalMessages()

    }

    private fun getLocalMessages() {
        repo.messages(LIMIT, offset)
            .subscribeOn(Schedulers.io())
            .subscribe({
                _loading.hide()
            }, { handleError(it) })
            .addTo(disposable)
    }

    fun loadNextMessages() {
        offset += LIMIT
        getLocalMessages()
    }


}