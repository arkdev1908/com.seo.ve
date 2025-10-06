package com.seo.visualexhibition.database.repository

import androidx.annotation.WorkerThread
import com.seo.visualexhibition.data.model.Template
import com.seo.visualexhibition.data.model.TemplateField
import com.seo.visualexhibition.database.dao.TemplateDao
import kotlinx.coroutines.flow.Flow

class TemplateRepository(private val templateDao: TemplateDao) {
    val all: Flow<List<Template>> = templateDao.all()

    fun template(id: Long): Flow<Template> {
        return templateDao.template(id)
    }

    fun allByTemplateId(id: Long): Flow<List<TemplateField>> {
        return templateDao.allByTemplateId(id)
    }

    @WorkerThread
    @JvmName("deleteByTemplateId")
    suspend fun deleteByTemplateId(id: Long) {
        return templateDao.deleteByTemplateId(id)
    }

    @WorkerThread
    @JvmName("insertTemplate")
    suspend fun insert(template: Template) {
        templateDao.insert(template)
    }

    @WorkerThread
    @JvmName("updateTemplate")
    suspend fun update(template: Template) {
        templateDao.update(template)
    }

    @WorkerThread
    @JvmName("deleteTemplate")
    suspend fun delete(template: Template) {
        templateDao.delete(template)
    }

    @WorkerThread
    @JvmName("insertTemplateField")
    suspend fun insert(templateField: TemplateField) {
        templateDao.insert(templateField)
    }

    @WorkerThread
    @JvmName("updateTemplateField")
    suspend fun update(templateField: TemplateField) {
        templateDao.update(templateField)
    }

    @WorkerThread
    @JvmName("deleteTemplateField")
    suspend fun delete(templateField: TemplateField) {
        templateDao.delete(templateField)
    }
}