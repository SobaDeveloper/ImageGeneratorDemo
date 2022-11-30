package com.example.imagegeneratordemo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.imagegeneratordemo.model.Prompt

@Database(entities = [Prompt::class], version = 1)
@TypeConverters(DataConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun promptDao(): PromptDao
}