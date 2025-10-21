package com.example.moodjournal.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "thought_records")
data class ThoughtRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val situation: String,
    val automaticThought: String,
    val emotion: String,
    val distortionType: String,
    val evidence: String,
    val alternativeThought: String,
    val timestamp: Long = System.currentTimeMillis()
)
