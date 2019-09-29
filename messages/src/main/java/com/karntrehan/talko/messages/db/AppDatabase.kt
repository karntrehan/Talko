package com.karntrehan.talko.messages.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class MessagesDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "messages"
    }

    abstract fun messagesDao(): MessagesDao
}