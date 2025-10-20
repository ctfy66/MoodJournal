package com.example.moodjournal.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodjournal.data.MoodDatabase
import com.example.moodjournal.data.MoodEntry
import com.example.moodjournal.data.MoodRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MoodViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: MoodRepository
    
    private val _currentUserId = MutableStateFlow<Long?>(null)
    val currentUserId: StateFlow<Long?> = _currentUserId.asStateFlow()
    
    private val _allEntries = MutableStateFlow<List<MoodEntry>>(emptyList())
    val allEntries: StateFlow<List<MoodEntry>> = _allEntries.asStateFlow()
    
    private val _recentEntries = MutableStateFlow<List<MoodEntry>>(emptyList())
    val recentEntries: StateFlow<List<MoodEntry>> = _recentEntries.asStateFlow()
    
    private val _weekEntries = MutableStateFlow<List<MoodEntry>>(emptyList())
    val weekEntries: StateFlow<List<MoodEntry>> = _weekEntries.asStateFlow()
    
    private val _monthEntries = MutableStateFlow<List<MoodEntry>>(emptyList())
    val monthEntries: StateFlow<List<MoodEntry>> = _monthEntries.asStateFlow()
    
    private val _entryCount = MutableStateFlow(0)
    val entryCount: StateFlow<Int> = _entryCount.asStateFlow()
    
    private val _streakDays = MutableStateFlow(0)
    val streakDays: StateFlow<Int> = _streakDays.asStateFlow()
    
    private val _mostCommonMood = MutableStateFlow("😐 平静")
    val mostCommonMood: StateFlow<String> = _mostCommonMood.asStateFlow()
    
    private val _moodDistribution = MutableStateFlow<Map<String, Float>>(emptyMap())
    val moodDistribution: StateFlow<Map<String, Float>> = _moodDistribution.asStateFlow()
    
    private val _topFactors = MutableStateFlow<List<Pair<String, Float>>>(emptyList())
    val topFactors: StateFlow<List<Pair<String, Float>>> = _topFactors.asStateFlow()
    
    init {
        val moodEntryDao = MoodDatabase.getDatabase(application).moodEntryDao()
        repository = MoodRepository(moodEntryDao)
    }
    
    /**
     * 设置当前用户ID并加载数据
     */
    fun setUserId(userId: Long) {
        println("MoodViewModel: setUserId called with userId=$userId, current=${_currentUserId.value}")
        _currentUserId.value = userId
        println("MoodViewModel: UserId updated to $userId, loading user data...")
        loadUserData(userId)
    }
    
    /**
     * 获取当前用户ID（用于调试）
     */
    fun getCurrentUserIdForDebug(): Long? = _currentUserId.value
    
    /**
     * 清除所有数据（登出时调用）
     */
    fun clearData() {
        println("MoodViewModel: Clearing all data")
        _currentUserId.value = null
        _allEntries.value = emptyList()
        _recentEntries.value = emptyList()
        _weekEntries.value = emptyList()
        _monthEntries.value = emptyList()
        _entryCount.value = 0
        _streakDays.value = 0
        _mostCommonMood.value = "😐 平静"
        _moodDistribution.value = emptyMap()
        _topFactors.value = emptyList()
    }
    
    /**
     * 加载当前用户的所有数据
     */
    private fun loadUserData(userId: Long) {
        viewModelScope.launch {
            // Load all entries
            repository.getAllEntries(userId).collect { entries ->
                _allEntries.value = entries
            }
        }
        
        viewModelScope.launch {
            // Load recent entries
            repository.getRecentEntries(userId, 10).collect { entries ->
                _recentEntries.value = entries
            }
        }
        
        viewModelScope.launch {
            // Load week entries
            repository.getWeekEntries(userId).collect { entries ->
                _weekEntries.value = entries
            }
        }
        
        viewModelScope.launch {
            // Load month entries
            repository.getMonthEntries(userId).collect { entries ->
                _monthEntries.value = entries
            }
        }
        
        viewModelScope.launch {
            // Load entry count
            repository.getEntryCount(userId).collect { count ->
                _entryCount.value = count
            }
        }
        
        // Calculate statistics - listen to data changes
        viewModelScope.launch {
            combine(_allEntries, _monthEntries) { all, month ->
                Pair(all, month)
            }.collect { (all, month) ->
                updateStatistics(userId, all, month)
            }
        }
    }
    
    fun insertEntry(moodLevel: Int, factors: List<String>, note: String) {
        val userId = _currentUserId.value
        if (userId == null) {
            println("MoodViewModel: Cannot insert entry - userId is null")
            return
        }
        println("MoodViewModel: Inserting entry for userId=$userId, moodLevel=$moodLevel")
        viewModelScope.launch {
            try {
                val entry = MoodEntry(
                    userId = userId,
                    moodLevel = moodLevel,
                    factors = factors,
                    note = note
                )
                val insertedId = repository.insertEntry(entry)
                println("MoodViewModel: Entry inserted successfully with id=$insertedId")
            } catch (e: Exception) {
                println("MoodViewModel: Error inserting entry - ${e.message}")
                e.printStackTrace()
            }
        }
    }
    
    fun updateEntry(entry: MoodEntry) {
        viewModelScope.launch {
            repository.updateEntry(entry)
            // Statistics will be updated automatically via the combine flow
        }
    }
    
    fun deleteEntry(entry: MoodEntry) {
        viewModelScope.launch {
            repository.deleteEntry(entry)
            // Statistics will be updated automatically via the combine flow
        }
    }
    
    fun deleteAllEntries() {
        val userId = _currentUserId.value ?: return
        viewModelScope.launch {
            repository.deleteAllEntries(userId)
            // Statistics will be updated automatically via the combine flow
        }
    }
    
    private suspend fun updateStatistics(userId: Long, allEntriesList: List<MoodEntry>, monthEntriesList: List<MoodEntry>) {
        // Update streak
        _streakDays.value = repository.getStreakDays(userId)
        
        // Update most common mood based on all entries
        if (allEntriesList.isNotEmpty()) {
            val moodCounts = allEntriesList.groupingBy { it.moodLevel }.eachCount()
            val mostCommon = moodCounts.maxByOrNull { it.value }?.key ?: 2
            val moodEntry = MoodEntry(userId = userId, moodLevel = mostCommon, factors = emptyList(), note = "")
            _mostCommonMood.value = "${moodEntry.getMoodEmoji()} ${moodEntry.getMoodName()}"
        }
        
        // Update mood distribution based on month entries
        if (monthEntriesList.isNotEmpty()) {
            val total = monthEntriesList.size.toFloat()
            val distribution = monthEntriesList.groupingBy { it.getMoodEmoji() }
                .eachCount()
                .mapValues { it.value / total }
            _moodDistribution.value = distribution
        } else {
            _moodDistribution.value = emptyMap()
        }
        
        // Update top factors based on month entries
        if (monthEntriesList.isNotEmpty()) {
            val factorCounts = mutableMapOf<String, Int>()
            monthEntriesList.forEach { entry ->
                entry.factors.forEach { factor ->
                    factorCounts[factor] = (factorCounts[factor] ?: 0) + 1
                }
            }
            
            if (factorCounts.isNotEmpty()) {
                val total = factorCounts.values.sum().toFloat()
                _topFactors.value = factorCounts.entries
                    .map { it.key to (it.value / total) }
                    .sortedByDescending { it.second }
                    .take(5)
            } else {
                _topFactors.value = emptyList()
            }
        } else {
            _topFactors.value = emptyList()
        }
    }
    
    fun getMoodDistribution(): Map<String, Float> {
        return moodDistribution.value
    }
    
    fun getTopFactors(): List<Pair<String, Float>> {
        return topFactors.value
    }
    
    fun getWeeklyMoodData(): List<Float> {
        val entries = weekEntries.value
        val weekData = MutableList(7) { 0f }
        
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"))
        val today = calendar.get(Calendar.DAY_OF_YEAR)
        
        entries.forEach { entry ->
            calendar.timeInMillis = entry.timestamp
            val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
            val daysAgo = today - dayOfYear
            
            if (daysAgo in 0..6) {
                val index = 6 - daysAgo
                // Average mood level for that day (0-4 range, normalized to 0-1)
                weekData[index] = (entry.moodLevel / 4f)
            }
        }
        
        return weekData
    }
    
    fun formatTimestamp(timestamp: Long): String {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"))
        val now = calendar.timeInMillis
        val diff = now - timestamp
        
        val beijingTimeZone = TimeZone.getTimeZone("Asia/Shanghai")
        
        return when {
            diff < 60 * 1000 -> "刚刚"
            diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)}分钟前"
            diff < 24 * 60 * 60 * 1000 -> {
                val sdf = SimpleDateFormat("HH:mm", Locale.CHINA)
                sdf.timeZone = beijingTimeZone
                "今天 ${sdf.format(Date(timestamp))}"
            }
            diff < 48 * 60 * 60 * 1000 -> {
                val sdf = SimpleDateFormat("HH:mm", Locale.CHINA)
                sdf.timeZone = beijingTimeZone
                "昨天 ${sdf.format(Date(timestamp))}"
            }
            else -> {
                val sdf = SimpleDateFormat("MM月dd日 HH:mm", Locale.CHINA)
                sdf.timeZone = beijingTimeZone
                sdf.format(Date(timestamp))
            }
        }
    }
}
