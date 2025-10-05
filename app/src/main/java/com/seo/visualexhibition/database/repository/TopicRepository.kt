package com.seo.visualexhibition.database.repository

import androidx.annotation.WorkerThread
import com.seo.visualexhibition.data.model.Topic
import com.seo.visualexhibition.database.dao.TopicDao
import kotlinx.coroutines.flow.Flow

class TopicRepository(private val topicDao: TopicDao) {
    val allTopics: Flow<List<Topic>> = topicDao.all();

    @WorkerThread
    suspend fun insert(topic: Topic) {
        topicDao.insert(topic);
    }

    @WorkerThread
    suspend fun update(topic: Topic) {
        topicDao.update(topic);
    }

    @WorkerThread
    suspend fun delete(topic: Topic) {
        topicDao.delete(topic);
    }
}