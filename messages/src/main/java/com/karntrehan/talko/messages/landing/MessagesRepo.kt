package com.karntrehan.talko.messages.landing

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.karntrehan.talko.messages.db.MessagesDao
import com.karntrehan.talko.messages.db.tables.User
import com.karntrehan.talko.messages.landing.models.MessagesAndUsersJsonModel
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import okio.buffer
import okio.source
import java.nio.charset.StandardCharsets


class MessagesRepo(
    private val prefs: SharedPreferences,
    private val gson: Gson,
    private val dao: MessagesDao
) : MessagesContract.Repo {

    private val FIRST_LOAD = "first_load"

    val TAG = "MessagesRepo"

    override fun isFirstLoad() = prefs.getBoolean(FIRST_LOAD, true)

    override fun loadMessagesIntoMemory(disposable: CompositeDisposable): PublishSubject<Boolean> {
        val subject = PublishSubject.create<Boolean>()
        Log.d(TAG, "First Load")
        val messagesAndUser = gson.fromJson(
            getResponseFromJson("messages"),
            MessagesAndUsersJsonModel::class.java
        )
        prefs.edit().putBoolean(FIRST_LOAD, false).apply()
        saveUsers(messagesAndUser, disposable, subject)
        saveNotifications(messagesAndUser, disposable, subject)
        saveAttachments(messagesAndUser, disposable, subject)
        return subject
    }

    override fun messages(limit: Int, offset: Int) = dao.messages(limit, offset)

    override fun currentUserId() = 1

    override fun user(userId: Int): User? = dao.user(userId)

    override fun attachments(messageId: Int) = dao.attachments(messageId)

    override fun deleteAttachment(attachmentId: String) = dao.deleteAttachment(attachmentId)

    override fun deleteMessage(id: Int) = dao.deleteMessage(id)

    private fun saveUsers(
        messagesAndUser: MessagesAndUsersJsonModel,
        disposable: CompositeDisposable,
        subject: PublishSubject<Boolean>
    ) {
        Maybe.just(messagesAndUser.users)
            .subscribeOn(Schedulers.single())
            .map { dao.insertUsers(it) }
            .doOnError { subject.onError(it) }
            .subscribe()
            .addTo(disposable)
    }

    private fun saveNotifications(
        messagesAndUser: MessagesAndUsersJsonModel,
        disposable: CompositeDisposable,
        subject: PublishSubject<Boolean>
    ) {
        Maybe.just(messagesAndUser.messages)
            .subscribeOn(Schedulers.single())
            .map { dao.insertMessages(it) }
            .doOnError { subject.onError(it) }
            .subscribe()
            .addTo(disposable)
    }

    private fun saveAttachments(
        messagesAndUser: MessagesAndUsersJsonModel,
        disposable: CompositeDisposable,
        subject: PublishSubject<Boolean>
    ) {
        Maybe.just(messagesAndUser.messages)
            .subscribeOn(Schedulers.single())
            .doOnError { subject.onError(it) }
            .map {
                it.forEach { message ->
                    message.attachments?.forEach { attachment ->
                        attachment.messageId = message.id
                        dao.insertAttachments(attachment)
                    }
                }
            }
            .subscribe { subject.onNext(true) }
            .addTo(disposable)
    }


    /**
     *Helper to read a JSON file and return a JSON string
     *Note: JSON file should have the structure "/api-response/filename.json"
     * @param fileName: File's name
     * @return JSON String
     */
    @Suppress("SameParameterValue")
    private fun getResponseFromJson(fileName: String): String {
        val inputStream = javaClass.classLoader?.getResourceAsStream("api-response/$fileName.json")
        return if (inputStream != null) {
            val source = inputStream.source().buffer()
            source.readString(StandardCharsets.UTF_8)
        } else ""
    }

}