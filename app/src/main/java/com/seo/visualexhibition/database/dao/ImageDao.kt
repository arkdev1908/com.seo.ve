package com.seo.visualexhibition.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.seo.visualexhibition.data.model.DisplayImage
import com.seo.visualexhibition.data.model.ImageField
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(displayImage: DisplayImage)

    @Insert
    suspend fun insert(displayImages: List<DisplayImage>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(displayImage: DisplayImage)

    @Delete
    suspend fun delete(displayImage: DisplayImage)

    @Query("SELECT * FROM display_images WHERE topic_id = :id")
    fun allByTopicId(id: Long): Flow<List<DisplayImage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(imageField: ImageField)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(imageField: ImageField)

    @Delete
    suspend fun delete(imageField: ImageField)

    @Delete
    suspend fun delete(imageFields: List<ImageField>)

    @Query("SELECT image_fields.* FROM image_fields " +
            "INNER JOIN template_fields ON template_fields.field_id = image_fields.field_id " +
            "WHERE image_id = :imageId ORDER BY order_number")
    fun allByImageId(imageId: Long): Flow<List<ImageField>>
}