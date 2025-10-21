package com.example.moodjournal.data

import kotlinx.coroutines.flow.Flow

class ThoughtRecordRepository(private val thoughtRecordDao: ThoughtRecordDao) {
    
    fun getAllThoughtRecords(userId: Long): Flow<List<ThoughtRecord>> {
        return thoughtRecordDao.getAllThoughtRecords(userId)
    }
    
    fun getRecentThoughtRecords(userId: Long): Flow<List<ThoughtRecord>> {
        return thoughtRecordDao.getRecentThoughtRecords(userId)
    }
    
    suspend fun getThoughtRecordById(userId: Long, id: Long): ThoughtRecord? {
        return thoughtRecordDao.getThoughtRecordById(userId, id)
    }
    
    suspend fun insertThoughtRecord(thoughtRecord: ThoughtRecord) {
        thoughtRecordDao.insertThoughtRecord(thoughtRecord)
    }
    
    suspend fun deleteThoughtRecord(thoughtRecord: ThoughtRecord) {
        thoughtRecordDao.deleteThoughtRecord(thoughtRecord)
    }
    
    suspend fun deleteAllForUser(userId: Long) {
        thoughtRecordDao.deleteAllForUser(userId)
    }
    
    fun getThoughtRecordCount(userId: Long): Flow<Int> {
        return thoughtRecordDao.getThoughtRecordCount(userId)
    }
}
