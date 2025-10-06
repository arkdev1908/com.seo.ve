package com.seo.visualexhibition.ui.template

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.seo.visualexhibition.data.model.Template
import com.seo.visualexhibition.data.model.TemplateField
import com.seo.visualexhibition.data.model.Topic
import com.seo.visualexhibition.database.repository.TemplateRepository
import com.seo.visualexhibition.database.repository.TopicRepository
import com.seo.visualexhibition.ui.topic.TopicViewModel
import kotlinx.coroutines.launch

class TemplateViewModel(private val repository: TemplateRepository) : ViewModel() {
    val all: LiveData<List<Template>> = repository.all.asLiveData()
    fun allByTemplateId(id: Long): LiveData<List<TemplateField>> = repository.allByTemplateId(id).asLiveData()

    fun insert(template: Template) = viewModelScope.launch {
        repository.insert(template)
    }

    fun insert(field: TemplateField) = viewModelScope.launch {
        repository.insert(field)
    }

    fun update(template: Template) = viewModelScope.launch {
        repository.update(template)
    }

    fun update(field: TemplateField) = viewModelScope.launch {
        repository.update(field)
    }

    fun delete(template: Template) = viewModelScope.launch {
        repository.delete(template)
    }

    fun delete(field: TemplateField) = viewModelScope.launch {
        repository.delete(field)
    }

    fun deleteByTemplateId(id: Long) = viewModelScope.launch {
        repository.deleteByTemplateId(id)
    }
}

class TemplateViewModelFactory(private val repository: TemplateRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TemplateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TemplateViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}