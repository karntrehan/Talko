package com.karntrehan.talko.messages.landing

import com.karntrehan.talko.messages.db.tables.Message
import com.karntrehan.talko.messages.db.tables.User
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

interface MessagesContract {
    interface Repo {

        fun isFirstLoad(): Boolean
        fun loadMessagesIntoMemory(disposable: CompositeDisposable): PublishSubject<Boolean>
        fun messages(limit: Int, offset: Int): Single<List<Message>>
        fun currentUserId(): Int
        fun user(userId: Int): User?
    }
}