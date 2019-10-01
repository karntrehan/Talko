package com.karntrehan.talko.messages.db.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "attachments",
    foreignKeys = [ForeignKey(
        entity = Message::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("message_id"),
        onDelete = CASCADE
    )]
)
data class Attachment(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "url") val url: String?,
    @ColumnInfo(name = "thumbnail_url") val thumbnailUrl: String?,
    @ColumnInfo(name = "message_id") var messageId: Int
)