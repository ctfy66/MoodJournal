package com.example.moodjournal.data

import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import java.util.TimeZone

class MoodRepository(private val moodEntryDao: MoodEntryDao) {
    
    fun getAllEntries(userId: Long): Flow<List<MoodEntry>> {
        return moodEntryDao.getAllEntries(userId)
    }
    
    fun getEntryCount(userId: Long): Flow<Int> {
        return moodEntryDao.getEntryCount(userId)
    }
    
    fun getRecentEntries(userId: Long, limit: Int = 10): Flow<List<MoodEntry>> {
        return moodEntryDao.getRecentEntries(userId, limit)
    }
    
    fun getEntriesInRange(userId: Long, startTime: Long, endTime: Long): Flow<List<MoodEntry>> {
        return moodEntryDao.getEntriesInRange(userId, startTime, endTime)
    }
    
    fun getEntriesByMood(userId: Long, moodLevel: Int): Flow<List<MoodEntry>> {
        return moodEntryDao.getEntriesByMood(userId, moodLevel)
    }
    
    suspend fun getEntryById(id: Long): MoodEntry? {
        return moodEntryDao.getEntryById(id)
    }
    
    suspend fun insertEntry(entry: MoodEntry): Long {
        return moodEntryDao.insertEntry(entry)
    }
    
    suspend fun updateEntry(entry: MoodEntry) {
        moodEntryDao.updateEntry(entry)
    }
    
    suspend fun deleteEntry(entry: MoodEntry) {
        moodEntryDao.deleteEntry(entry)
    }
    
    suspend fun deleteAllEntries(userId: Long) {
        moodEntryDao.deleteAllEntries(userId)
    }
    
    // Helper functions
    fun getWeekEntries(userId: Long): Flow<List<MoodEntry>> {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"))
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.add(Calendar.DAY_OF_YEAR, -6) // Last 7 days
        
        val startTime = calendar.timeInMillis
        
        // Use getEntriesSince which doesn't have an end time limit
        return moodEntryDao.getEntriesSince(userId, startTime)
    }
    
    fun getMonthEntries(userId: Long): Flow<List<MoodEntry>> {
        // Use database-side date filtering to avoid fixed time range issue
        return moodEntryDao.getCurrentMonthEntries(userId)
    }
    
    suspend fun getStreakDays(userId: Long): Int {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"))
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        
        var streak = 0
        var currentDate = calendar.timeInMillis
        
        // Check last 30 days max
        for (i in 0..30) {
            val startOfDay = currentDate
            val endOfDay = startOfDay + (24 * 60 * 60 * 1000) - 1
            
            val count = moodEntryDao.getEntryCountSince(userId, startOfDay)
            if (count > 0) {
                streak++
                currentDate -= (24 * 60 * 60 * 1000)
            } else {
                break
            }
        }
        
        return streak
    }
}
