package com.karntrehan.talko.messages.db.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey var id: Int = 0,
    @ColumnInfo(name = "user_id") var userId: Int = 0,
    @ColumnInfo(name = "content") var content: String? = null,
    @Ignore var attachments: List<Attachment>? = null
)