package com.seo.visualexhibition.ui.image

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Transaction
import com.seo.visualexhibition.data.model.DisplayImage
import com.seo.visualexhibition.data.model.ImageField
import com.seo.visualexhibition.database.repository.ImageRepository
import kotlinx.coroutines.launch

class ImageViewModel(private val repository: ImageRepository) : ViewModel() {
    fun all(id: Long): LiveData<List<DisplayImage>> = repository.allByTopicId(id).asLiveData()

    @Transaction
    fun insertImageWithFields(image: DisplayImage, fields: List<ImageField>) = viewModelScope.launch {
        val imageId = repository.insert(image)
        val updatedFields = fields.map { it.copy(id = imageId) }
        repository.insert(updatedFields)
    }

    fun update(image: DisplayImage) = viewModelScope.launch {
        repository.update(image)
    }

    fun delete(image: DisplayImage) = viewModelScope.launch {
        repository.delete(image)
    }

    private val _selectedImages = MutableLiveData<List<Uri>>(emptyList())
    val selectedImages: LiveData<List<Uri>> = _selectedImages

    fun setSelectedImages(uris: List<Uri>) {
        _selectedImages.value = uris
    }

    fun clearAllImages() {
        _selectedImages.value = emptyList()
    }

    private val _templateId = MutableLiveData<Long>(0)
    val templateId: LiveData<Long> = _templateId

    fun setTemplateId(id: Long) {
        _templateId.value = id
    }

    private val _topicId = MutableLiveData<Long>(0)
    val topicId: LiveData<Long> = _topicId

    fun setTopicId(id: Long) {
        _topicId.value = id
    }
}

class ImageViewModelFactory(private val repository: ImageRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ImageViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}