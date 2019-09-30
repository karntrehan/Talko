package com.karntrehan.talko.messages.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.karntrehan.talko.messages.db.tables.Attachment
import com.karntrehan.talko.messages.db.tables.Message
import com.karntrehan.talko.messages.db.tables.User
import io.reactivex.Single

@Dao
interface MessagesDao {

    /*Insertions*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(users: List<User>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessages(messages: List<Message>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAttachments(vararg attachments: Attachment)

    /*Queries*/
    @Query("SELECT * FROM MESSAGES LIMIT :limit OFFSET :offset")
    fun messages(limit: Int, offset: Int): Single<List<Message>>

    @Query("SELECT * FROM USERS WHERE id = :userId LIMIT 1")
    fun user(userId: Int): User
}