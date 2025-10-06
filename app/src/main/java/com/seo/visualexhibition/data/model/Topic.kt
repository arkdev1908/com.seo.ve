package com.seo.visualexhibition.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topics")
data class Topic(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "topic_id") val id: Long = 0,
    @ColumnInfo(name = "topic_name") val topicName: String,
    @ColumnInfo(name = "topic_description") val topicDescription: String?,
    @ColumnInfo(name = "image_src") val imageSrc: String
)
