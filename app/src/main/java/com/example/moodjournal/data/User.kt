package com.example.moodjournal.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val username: String,
    
    val passwordHash: String, // SHA-256 hashed password
    
    val email: String? = null,
    
    val createdAt: Long = System.currentTimeMillis(),
    
    val displayName: String? = null
) {
    companion object {
        // 用户名验证规则
        fun isValidUsername(username: String): Boolean {
            return username.length in 3..20 && username.matches(Regex("^[a-zA-Z0-9_\\u4e00-\\u9fa5]+$"))
        }
        
        // 密码验证规则（至少6位）
        fun isValidPassword(password: String): Boolean {
            return password.length >= 6
        }
        
        // 邮箱验证规则（可选）
        fun isValidEmail(email: String): Boolean {
            if (email.isEmpty()) return true // 邮箱是可选的
            return email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
        }
    }
}
