package com.seo.visualexhibition.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "events")
data class Event(
    @ColumnInfo(name = "event_id") val id: Long = 0,
    @ColumnInfo(name = "event_name") val eventName: String,
    @ColumnInfo(name = "event_content") val eventContent: String
)