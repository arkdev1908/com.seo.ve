package com.seo.visualexhibition.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "templates")
data class Template(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "template_id") val id: Int = 0,
    @ColumnInfo(name = "template_name") val templateName: String,
)

@Entity(tableName = "template_fields")
data class TemplateField(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "field_id") val id: Int,
    @ColumnInfo(name = "template_id") val templateId: Int,
    @ColumnInfo(name = "field_name") val fieldName: String,
    @ColumnInfo(name = "order_number") val orderNumber: Int
)
