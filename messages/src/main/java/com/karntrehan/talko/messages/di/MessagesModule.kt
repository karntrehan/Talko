package com.karntrehan.talko.messages.di

import android.content.Context
import androidx.room.Room
import com.karntrehan.talko.messages.db.MessagesDatabase
import com.karntrehan.talko.messages.landing.MessagesContract
import com.karntrehan.talko.messages.landing.MessagesRepo
import com.karntrehan.talko.messages.landing.MessagesVMF
import dagger.Module
import dagger.Provides

@Module
class MessagesModule {

    @MessagesScope
    @Provides
    fun messagesVMF(repo: MessagesContract.Repo): MessagesVMF = MessagesVMF(repo)

    @MessagesScope
    @Provides
    fun messagesContract(database: MessagesDatabase): MessagesContract.Repo =
        MessagesRepo(database.messagesDao())

    @MessagesScope
    @Provides
    fun database(context: Context): MessagesDatabase = Room
        .databaseBuilder(context, MessagesDatabase::class.java, MessagesDatabase.DB_NAME)
        .build()

}