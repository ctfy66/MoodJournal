package com.example.moodjournal.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodjournal.data.MoodDatabase
import com.example.moodjournal.data.ThoughtRecord
import com.example.moodjournal.data.ThoughtRecordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ThoughtRecordViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ThoughtRecordRepository
    
    private var currentUserId: Long? = null
    
    private val _allThoughtRecords = MutableStateFlow<List<ThoughtRecord>>(emptyList())
    val allThoughtRecords: StateFlow<List<ThoughtRecord>> = _allThoughtRecords.asStateFlow()
    
    private val _recentThoughtRecords = MutableStateFlow<List<ThoughtRecord>>(emptyList())
    val recentThoughtRecords: StateFlow<List<ThoughtRecord>> = _recentThoughtRecords.asStateFlow()
    
    private val _thoughtRecordCount = MutableStateFlow(0)
    val thoughtRecordCount: StateFlow<Int> = _thoughtRecordCount.asStateFlow()
    
    init {
        val thoughtRecordDao = MoodDatabase.getDatabase(application).thoughtRecordDao()
        repository = ThoughtRecordRepository(thoughtRecordDao)
    }
    
    fun setUserId(userId: Long) {
        println("ThoughtRecordViewModel: Setting userId to $userId")
        currentUserId = userId
        loadThoughtRecords()
    }
    
    fun clearData() {
        println("ThoughtRecordViewModel: Clearing data")
        currentUserId = null
        _allThoughtRecords.value = emptyList()
        _recentThoughtRecords.value = emptyList()
        _thoughtRecordCount.value = 0
    }
    
    private fun loadThoughtRecords() {
        val userId = currentUserId ?: return
        
        viewModelScope.launch {
            repository.getAllThoughtRecords(userId).collect { records ->
                println("ThoughtRecordViewModel: Loaded ${records.size} thought records")
                _allThoughtRecords.value = records
            }
        }
        
        viewModelScope.launch {
            repository.getRecentThoughtRecords(userId).collect { records ->
                _recentThoughtRecords.value = records
            }
        }
        
        viewModelScope.launch {
            repository.getThoughtRecordCount(userId).collect { count ->
                _thoughtRecordCount.value = count
            }
        }
    }
    
    fun insertThoughtRecord(
        situation: String,
        automaticThought: String,
        emotion: String,
        distortionType: String,
        evidence: String,
        alternativeThought: String
    ) {
        val userId = currentUserId
        if (userId == null) {
            println("ThoughtRecordViewModel: Cannot insert, userId is null")
            return
        }
        
        viewModelScope.launch {
            val thoughtRecord = ThoughtRecord(
                userId = userId,
                situation = situation,
                automaticThought = automaticThought,
                emotion = emotion,
                distortionType = distortionType,
                evidence = evidence,
                alternativeThought = alternativeThought
            )
            repository.insertThoughtRecord(thoughtRecord)
            println("ThoughtRecordViewModel: Inserted thought record for userId=$userId")
        }
    }
    
    fun deleteThoughtRecord(thoughtRecord: ThoughtRecord) {
        viewModelScope.launch {
            repository.deleteThoughtRecord(thoughtRecord)
            println("ThoughtRecordViewModel: Deleted thought record id=${thoughtRecord.id}")
        }
    }
    
    fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
    
    fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("MM月dd日", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
    
    fun getCurrentUserIdForDebug(): Long? {
        return currentUserId
    }
}
