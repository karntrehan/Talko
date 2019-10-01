package com.karntrehan.talko.messages.landing

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
    //Expose LiveData to observers rather than MutableLiveData
    val messages: LiveData<List<MessageModel>> by lazy { _messages }

    private val currentUserId: Int by lazy { repo.currentUserId() }

    fun messages() {

        if (!_messages.value.isNullOrEmpty()) return

        _loading.show()
        if (repo.isFirstLoad()) {
            //Trigger repo to load data into rooom. Listen to it's status through a Subject.
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
        repo.messages(LIMIT, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { addUsersAndAttachments(it) }
            .map { appendOrSetResults(it) }
            .subscribe({
                _loading.hide()
                _messages.postValue(it)
            }, { handleError(it) })
            .addTo(disposable)
    }

    private fun addUsersAndAttachments(messages: List<Message>): MutableList<MessageModel> {
        val result = mutableListOf<MessageModel>()
        var prevUserId = -1

        messages.forEach { message ->
            if (message.userId == currentUserId) {
                //This message is from current User
                if (prevUserId != currentUserId)
                //If this is first message from current user, add SentName element
                    result.add(SentName(currentUserId))
                //Add the current user's message
                result.add(SentMessage(message.id, message.content))
                //Add current user's attachments of this message
                addSelfAttachments(message.id, result)
            } else if (message.userId == prevUserId) {
                //The message belongs to the previous, hence just add the msg
                // and the related attachments
                result.add(ReceivedMessage(message.id, message.content))
                addReceivedAttachments(message.id, result)
            } else {
                //This is a new user, hence add the ReceivedName element along with msg
                // and the related attachments
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

    private fun appendOrSetResults(newResults: MutableList<MessageModel>): List<MessageModel> {
        val combinedResult = mutableListOf<MessageModel>()

        //If there is already data on the screen add it first to the combined result
        _messages.value?.let {
            combinedResult.addAll(it)
        }

        //Append new results to the combined result
        combinedResult.addAll(newResults)

        return combinedResult
    }

    fun loadNextMessages() {
        offset += LIMIT
        getLocalMessages()
    }

    fun deleteAttachment(position: Int, attachmentId: String) {
        _loading.show()
        //Delete attachment from local source
        repo.deleteAttachment(attachmentId)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map {
                //Make a copy of current data, using same list causes issues with ListAdapter
                _messages.value?.map { it }?.toMutableList()?.let { currentData ->
                    //Remove just the attachment
                    currentData.removeAt(position)
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

    fun deleteMessage(position: Int, id: Int) {
        _loading.show()
        //Delete message from local source, as it is linked to attachments,
        // those would also be "cascade deleted"
        repo.deleteMessage(id)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map {
                //Make a copy of current data, using same list causes issues with ListAdapter
                _messages.value?.map { it }?.toMutableList()?.let { currentData ->

                    val removedMsg = currentData.removeAt(position)

                    //Remove all the attachments for this message
                    while (currentData[position] is SentAttachment || currentData[position] is ReceivedAttachment) {
                        currentData.removeAt(position)
                    }

                    //If this was the only msg from the user, delete their name
                    if (currentData[position] is SentName || currentData[position] is ReceivedName) {
                        currentData.removeAt(position - 1)
                    }
                    //The next msg is from the same user, add their avatar to their msg
                    else if (currentData[position] is ReceivedMessage && removedMsg is ReceivedMessage) {
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