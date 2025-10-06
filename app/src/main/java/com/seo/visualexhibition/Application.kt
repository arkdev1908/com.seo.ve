package com.seo.visualexhibition

import android.app.Application
import com.seo.visualexhibition.database.AppDatabase
import com.seo.visualexhibition.database.repository.ImageRepository
import com.seo.visualexhibition.database.repository.TemplateRepository
import com.seo.visualexhibition.database.repository.TopicRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class Application: Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val topicRepository by lazy { TopicRepository(database.topicDao()) }
    val templateRepository by lazy { TemplateRepository(database.templateDao())}
    val imageRepository by lazy { ImageRepository(database.imageDao()) }
}