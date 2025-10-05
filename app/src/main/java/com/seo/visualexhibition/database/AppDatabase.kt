package com.seo.visualexhibition.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.seo.visualexhibition.data.model.Topic
import com.seo.visualexhibition.database.dao.TopicDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Topic::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun topicDao(): TopicDao

    private class AppDatabaseCallback(private val scope: CoroutineScope) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val topicDao = database.topicDao()
                    topicDao.deleteAll()
                    val sampleTopics = listOf(
                        Topic(
                            topicName = "Chủ đề 1.",
                            topicDescription = "Đây là mô tả ngắn cho chủ đề 1. Nếu quá dài thì sẽ bị cắt.Đây là mô tả ngắn cho chủ đề 1. Nếu quá dài thì sẽ bị cắt. Đây là mô tả ngắn cho chủ đề 1. Nếu quá dài thì sẽ bị cắt. Đây là mô tả ngắn cho chủ đề 1. Nếu quá dài thì sẽ bị cắt.",
                            imageSrc = "/storage/emulated/0/Download/sample1.jpg",
                        ),
                        Topic(
                            topicName = "Chủ đề 2.",
                            topicDescription = "Một mô tả khác cho chủ đề 2. Mục đích để test hiển thị nhiều dòng.",
                            imageSrc = "/storage/emulated/0/Download/sample3.jpg",
                        ),
                        Topic(
                            topicName = "Chủ đề 3.",
                            topicDescription = "Một mô tả khác cho chủ đề 2. Mục đích để test hiển thị nhiều dòng.",
                            imageSrc = "/storage/emulated/0/Download/sample3.jpg",
                        )
                    )
                    topicDao.insert(sampleTopics)
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}