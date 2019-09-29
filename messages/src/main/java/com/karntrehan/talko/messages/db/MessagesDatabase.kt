package com.karntrehan.talko.messages.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.karntrehan.talko.messages.db.tables.Attachment
import com.karntrehan.talko.messages.db.tables.Message
import com.karntrehan.talko.messages.db.tables.User

@Database(entities = [User::class, Message::class, Attachment::class], version = 1)
abstract class MessagesDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "messages"
    }

    abstract fun messagesDao(): MessagesDao
}