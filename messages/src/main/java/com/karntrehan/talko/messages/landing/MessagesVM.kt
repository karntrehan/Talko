package com.karntrehan.talko.messages.landing

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.karntrehan.talko.architecture.BaseVM
import com.karntrehan.talko.extensions.hide
import com.karntrehan.talko.extensions.show
import com.karntrehan.talko.messages.db.tables.Message
import com.karntrehan.talko.messages.landing.models.*
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class MessagesVM(private val repo: MessagesContract.Repo) : BaseVM() {

    private val LIMIT = 20
    private var offset = 0

    val TAG = "MessagesVM"

    private val _messages = MutableLiveData<List<MessageModel>>()
    val messages: LiveData<List<MessageModel>> by lazy { _messages }

    private val currentUserId: Int by lazy { repo.currentUserId() }

    fun messages() {
        _loading.show()
        if (repo.isFirstLoad()) {
            repo.loadMessagesIntoMemory(disposable)
                .subscribe(
                    { if (it) getLocalMessages() },
                    { handleError(it) }
                )
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
            .map { appendOrSetResults(it) }
            .subscribe({
                _loading.hide()
                //Log.d(TAG, "getLocalMessages: $it")
                _messages.postValue(it)
            }, { handleError(it) })
            .addTo(disposable)
    }

    private fun addUsersToMessages(messages: List<Message>): MutableList<MessageModel> {
        val result = mutableListOf<MessageModel>()
        var prevUserId = -1

        messages.forEach { message ->
            if (message.userId == currentUserId) {
                if (prevUserId != currentUserId)
                    result.add(SentName(currentUserId))
                result.add(SentMessage(message.id, message.content))
                addSelfAttachments(message.id, result)
            } else if (message.userId == prevUserId) {
                result.add(ReceivedMessage(message.id, message.content))
                addReceivedAttachments(message.id, result)
            } else {
                repo.user(message.userId)?.let { currentUser ->
                    prevUserId = currentUser.id
                    result.add(ReceivedName(prevUserId, currentUser.name))
                    result.add(ReceivedMessage(message.id, message.content, currentUser.avatarId))
                    addReceivedAttachments(message.id, result)
                }
            }
        }

        return result
    }

    private fun addSelfAttachments(messageId: Int, result: MutableList<MessageModel>) {
        repo.attachments(messageId)
            ?.map { SentAttachment(it.id, it.thumbnailUrl, it.title) }
            ?.let { result.addAll(it) }
    }

    private fun addReceivedAttachments(messageId: Int, result: MutableList<MessageModel>) {
        repo.attachments(messageId)
            ?.map { ReceivedAttachment(it.id, it.thumbnailUrl, it.title) }
            ?.let { result.addAll(it) }
    }

    private fun appendOrSetResults(results: MutableList<MessageModel>): List<MessageModel> {

        val combinedResult = mutableListOf<MessageModel>()

        val currentData = _messages.value
        if (currentData != null)
            combinedResult.addAll(currentData)

        combinedResult.addAll(results)

        return combinedResult
    }

    fun loadNextMessages() {
        offset += LIMIT
        getLocalMessages()
    }

    fun deleteAttachment(position: Int, attachmentId: String) {
        _loading.show()
        repo.deleteAttachment(attachmentId)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map {
                val currentData = _messages.value?.map { it }?.toMutableList()
                currentData?.removeAt(position)
                return@map currentData ?: emptyList<MessageModel>()
            }
            .subscribe(
                {
                    _loading.hide()
                    _messages.postValue(it)
                }, { handleError(it) }
            )
            .addTo(disposable)
    }

    fun deleteMessage(position: Int, id: Int) {
        _loading.show()
        repo.deleteMessage(id)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map {
                _messages.value?.map { it }?.toMutableList()?.let { currentData ->

                    val removedMsg = currentData.removeAt(position)

                    while (currentData[position] is SentAttachment || currentData[position] is ReceivedAttachment) {
                        currentData.removeAt(position)
                    }

                    if (currentData[position] is SentName || currentData[position] is ReceivedName) {
                        currentData.removeAt(position - 1)
                    } else if (currentData[position] is ReceivedMessage && removedMsg is ReceivedMessage) {
                        currentData[position] =
                            (currentData[position] as ReceivedMessage).copy(avatarUrl = removedMsg.avatarUrl)
                    }
                    return@map currentData
                } ?: emptyList<MessageModel>()
            }
            .subscribe(
                {
                    _loading.hide()
                    _messages.postValue(it)
                }, { handleError(it) }
            )
            .addTo(disposable)
    }
}