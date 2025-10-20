package com.example.moodjournal.utils

import java.security.MessageDigest

object PasswordUtils {
    
    /**
     * 使用 SHA-256 对密码进行哈希加密
     */
    fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
    
    /**
     * 验证密码是否匹配
     */
    fun verifyPassword(inputPassword: String, storedHash: String): Boolean {
        val inputHash = hashPassword(inputPassword)
        return inputHash == storedHash
    }
    
    /**
     * 检查密码强度
     * 返回: 0-弱, 1-中等, 2-强
     */
    fun checkPasswordStrength(password: String): Int {
        if (password.length < 6) return 0
        
        var strength = 0
        
        // 长度检查
        if (password.length >= 8) strength++
        if (password.length >= 12) strength++
        
        // 包含数字
        if (password.any { it.isDigit() }) strength++
        
        // 包含大写字母
        if (password.any { it.isUpperCase() }) strength++
        
        // 包含小写字母
        if (password.any { it.isLowerCase() }) strength++
        
        // 包含特殊字符
        if (password.any { !it.isLetterOrDigit() }) strength++
        
        return when {
            strength <= 2 -> 0 // 弱
            strength <= 4 -> 1 // 中等
            else -> 2 // 强
        }
    }
}
