package com.seo.visualexhibition.ui.topic

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.seo.visualexhibition.data.model.Topic
import com.seo.visualexhibition.database.repository.TopicRepository
import kotlinx.coroutines.launch

class TopicViewModel(private val repository: TopicRepository) : ViewModel() {
    val all: LiveData<List<Topic>> = repository.allTopics.asLiveData()

    fun delete(topic: Topic) = viewModelScope.launch {
        repository.delete(topic)
    }
}

class TopicViewModelFactory(private val repository: TopicRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TopicViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TopicViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}