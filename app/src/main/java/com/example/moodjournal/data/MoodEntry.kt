package com.example.moodjournal.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

@Entity(tableName = "mood_entries")
@TypeConverters(Converters::class)
data class MoodEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val userId: Long, // 所属用户ID - 用于数据隔离
    
    val moodLevel: Int, // 0: 😠, 1: 😔, 2: 😐, 3: 🙂, 4: 😄
    
    val factors: List<String>, // Selected factors like "Work", "Sleep", etc.
    
    val note: String, // Journal text
    
    val timestamp: Long = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai")).timeInMillis,
    
    val createdDate: Date = Date(Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai")).timeInMillis)
) {
    fun getMoodEmoji(): String {
        return when (moodLevel) {
            0 -> "😠"
            1 -> "😔"
            2 -> "😐"
            3 -> "🙂"
            4 -> "😄"
            else -> "😐"
        }
    }
    
    fun getMoodName(): String {
        return when (moodLevel) {
            0 -> "愤怒"
            1 -> "难过"
            2 -> "平静"
            3 -> "开心"
            4 -> "非常开心"
            else -> "平静"
        }
    }
}

class Converters {
    private val gson = Gson()
    
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return gson.toJson(value)
    }
    
    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }
    
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
    
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }
}
