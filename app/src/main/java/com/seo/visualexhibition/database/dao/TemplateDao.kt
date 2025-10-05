package com.seo.visualexhibition.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.seo.visualexhibition.data.model.Template
import com.seo.visualexhibition.data.model.TemplateField
import kotlinx.coroutines.flow.Flow

@Dao
interface TemplateDao {
    @Insert
    suspend fun insert(template: Template)

    @Update
    suspend fun update(template: Template)

    @Delete
    suspend fun delete(template: Template)

    @Query("SELECT * FROM templates ORDER BY template_id")
    fun all(): Flow<List<Template>>

    @Query("SELECT * FROM templates WHERE template_id = :id")
    fun template(id: Long): Flow<Template>

    @Query("SELECT * FROM template_fields WHERE template_id = :id ORDER BY order_number")
    fun allByTemplateId(id: Long): Flow<List<TemplateField>>

    @Insert
    suspend fun insert(templateField: TemplateField)

    @Update
    suspend fun update(templateField: TemplateField)

    @Delete
    suspend fun delete(templateField: TemplateField)
}