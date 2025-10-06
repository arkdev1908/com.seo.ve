package com.seo.visualexhibition.database.repository

import androidx.annotation.WorkerThread
import com.seo.visualexhibition.data.model.DisplayImage
import com.seo.visualexhibition.data.model.ImageField
import com.seo.visualexhibition.database.dao.ImageDao
import kotlinx.coroutines.flow.Flow

class ImageRepository(private val imageDao: ImageDao) {
    fun allByTopicId(id: Long) : Flow<List<DisplayImage>> = imageDao.allByTopicId(id)

    fun allByImageId(imageId: Long) : Flow<List<ImageField>> = imageDao.allByImageId(imageId)

    @WorkerThread
    @JvmName("insertDisplayImage")
    suspend fun insert(displayImage: DisplayImage) : Long = imageDao.insert(displayImage)

    @WorkerThread
    @JvmName("updateDisplayImage")
    suspend fun update(displayImage: DisplayImage) {
        imageDao.update(displayImage);
    }

    @WorkerThread
    @JvmName("deleteDisplayImage")
    suspend fun delete(displayImage: DisplayImage) {
        imageDao.delete(displayImage);
    }

    @WorkerThread
    @JvmName("insertImageField")
    suspend fun insert(imageFields: List<ImageField>) {
        imageDao.insert(imageFields);
    }

    @WorkerThread
    @JvmName("updateImageField")
    suspend fun update(imageField: ImageField) {
        imageDao.update(imageField);
    }

    @WorkerThread
    @JvmName("deleteImageField")
    suspend fun delete(imageField: ImageField) {
        imageDao.delete(imageField);
    }

    @WorkerThread
    @JvmName("deleteImageFieldList")
    suspend fun delete(imageFields: List<ImageField>) {
        imageDao.delete(imageFields);
    }
}