package com.example.moodjournal.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface ThoughtRecordDao {
    @Query("SELECT * FROM thought_records WHERE userId = :userId ORDER BY timestamp DESC")
    fun getAllThoughtRecords(userId: Long): Flow<List<ThoughtRecord>>
    
    @Query("SELECT * FROM thought_records WHERE userId = :userId ORDER BY timestamp DESC LIMIT 10")
    fun getRecentThoughtRecords(userId: Long): Flow<List<ThoughtRecord>>
    
    @Query("SELECT * FROM thought_records WHERE userId = :userId AND id = :id")
    suspend fun getThoughtRecordById(userId: Long, id: Long): ThoughtRecord?
    
    @Insert
    suspend fun insertThoughtRecord(thoughtRecord: ThoughtRecord)
    
    @Delete
    suspend fun deleteThoughtRecord(thoughtRecord: ThoughtRecord)
    
    @Query("DELETE FROM thought_records WHERE userId = :userId")
    suspend fun deleteAllForUser(userId: Long)
    
    @Query("SELECT COUNT(*) FROM thought_records WHERE userId = :userId")
    fun getThoughtRecordCount(userId: Long): Flow<Int>
}
