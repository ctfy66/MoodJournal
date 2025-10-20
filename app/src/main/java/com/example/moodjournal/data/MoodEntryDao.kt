package com.example.moodjournal.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodEntryDao {
    
    @Query("SELECT * FROM mood_entries WHERE userId = :userId ORDER BY timestamp DESC")
    fun getAllEntries(userId: Long): Flow<List<MoodEntry>>
    
    @Query("SELECT * FROM mood_entries WHERE id = :id")
    suspend fun getEntryById(id: Long): MoodEntry?
    
    @Query("SELECT * FROM mood_entries WHERE userId = :userId ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentEntries(userId: Long, limit: Int = 10): Flow<List<MoodEntry>>
    
    @Query("SELECT * FROM mood_entries WHERE userId = :userId AND timestamp >= :startTime AND timestamp <= :endTime ORDER BY timestamp DESC")
    fun getEntriesInRange(userId: Long, startTime: Long, endTime: Long): Flow<List<MoodEntry>>
    
    @Query("SELECT * FROM mood_entries WHERE userId = :userId AND moodLevel = :moodLevel ORDER BY timestamp DESC")
    fun getEntriesByMood(userId: Long, moodLevel: Int): Flow<List<MoodEntry>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: MoodEntry): Long
    
    @Update
    suspend fun updateEntry(entry: MoodEntry)
    
    @Delete
    suspend fun deleteEntry(entry: MoodEntry)
    
    @Query("DELETE FROM mood_entries WHERE userId = :userId")
    suspend fun deleteAllEntries(userId: Long)
    
    @Query("SELECT COUNT(*) FROM mood_entries WHERE userId = :userId")
    fun getEntryCount(userId: Long): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM mood_entries WHERE userId = :userId AND timestamp >= :startTime")
    suspend fun getEntryCountSince(userId: Long, startTime: Long): Int
    
    @Query("""
        SELECT * FROM mood_entries 
        WHERE userId = :userId AND strftime('%Y-%m', datetime(timestamp/1000, 'unixepoch')) = strftime('%Y-%m', datetime('now'))
        ORDER BY timestamp DESC
    """)
    fun getCurrentMonthEntries(userId: Long): Flow<List<MoodEntry>>
    
    @Query("""
        SELECT * FROM mood_entries 
        WHERE userId = :userId AND timestamp >= :startTime
        ORDER BY timestamp DESC
    """)
    fun getEntriesSince(userId: Long, startTime: Long): Flow<List<MoodEntry>>
}
