package com.example.moodjournal.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [MoodEntry::class, User::class, ThoughtRecord::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MoodDatabase : RoomDatabase() {
    
    abstract fun moodEntryDao(): MoodEntryDao
    abstract fun userDao(): UserDao
    abstract fun thoughtRecordDao(): ThoughtRecordDao
    
    companion object {
        @Volatile
        private var INSTANCE: MoodDatabase? = null
        
        fun getDatabase(context: Context): MoodDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoodDatabase::class.java,
                    "mood_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
