package com.seo.visualexhibition.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "display_images")
data class DisplayImage(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "image_id") val id: Int,
    @ColumnInfo(name = "topic_id") val topicId: Int,
    @ColumnInfo(name = "template_id") val templateId: Int,
    @ColumnInfo(name = "image_name") val imageName: String,
    @ColumnInfo(name = "image_src") val imageSrc: String,
)

@Entity(tableName = "image_fields",
    primaryKeys = ["image_id", "field_id"])
data class ImageField(
    @ColumnInfo(name = "image_id") val id: Int,
    @ColumnInfo(name = "field_id") val fieldId: Int,
    @ColumnInfo(name = "field_value") val fieldValue: String,
)
