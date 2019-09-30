package com.karntrehan.talko.messages.landing

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.karntrehan.talko.architecture.BaseVM
import com.karntrehan.talko.extensions.hide
import com.karntrehan.talko.extensions.show
import com.karntrehan.talko.messages.db.tables.Message
import com.karntrehan.talko.messages.landing.models.ReceivedMessage
import com.karntrehan.talko.messages.landing.models.ReceivedName
import com.karntrehan.talko.messages.landing.models.SentMessage
import com.karntrehan.talko.messages.landing.models.SentName
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class MessagesVM(private val repo: MessagesContract.Repo) : BaseVM() {

    private val LIMIT = 20
    private var offset = 0

    val TAG = "MessagesVM"

    private val _messages = MutableLiveData<List<Any>>()
    val messages: LiveData<List<Any>> by lazy { _messages }

    val currentUserId: Int by lazy { repo.currentUserId() }

    fun messages() {
        _loading.show()
        if (repo.isFirstLoad()) {
            repo.loadMessagesIntoMemory(disposable)
                .subscribe { if (it) getLocalMessages() }
                .addTo(disposable)
            return
        }

        getLocalMessages()

    }

    private fun getLocalMessages() {
        Log.d(TAG, "getLocalMessages: $LIMIT $offset")
        repo.messages(LIMIT, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { addUsersToMessages(it) }
            .subscribe({
                Log.d(TAG, "getLocalMessages: $it")
                _loading.hide()
                _messages.postValue(it)
            }, { handleError(it) })
            .addTo(disposable)
    }

    private fun addUsersToMessages(messages: List<Message>): MutableList<Any> {
        val result = mutableListOf<Any>()
        var prevUserId = -1

        messages.forEach { message ->
            if (message.userId == currentUserId) {
                if (prevUserId != currentUserId)
                    result.add(SentName())
                result.add(SentMessage(message.content))
            } else if (message.userId == prevUserId) {
                result.add(ReceivedMessage(message.content))
            } else {
                repo.user(message.userId)?.let { currentUser ->
                    result.add(ReceivedName(currentUser.name))
                    result.add(ReceivedMessage(message.content, currentUser.avatarId))
                }
            }
        }

        return result
    }

    fun loadNextMessages() {
        Log.d(TAG, "loadNextMessages: ")
        offset += LIMIT
        getLocalMessages()
    }

}