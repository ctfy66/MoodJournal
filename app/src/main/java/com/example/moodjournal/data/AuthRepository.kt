package com.example.moodjournal.data

import android.content.Context
import android.content.SharedPreferences
import com.example.moodjournal.utils.PasswordUtils
import kotlinx.coroutines.flow.Flow

class AuthRepository(context: Context, private val userDao: UserDao) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "auth_prefs",
        Context.MODE_PRIVATE
    )
    
    companion object {
        private const val KEY_USER_ID = "current_user_id"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USERNAME = "username"
    }
    
    /**
     * 用户注册
     */
    suspend fun register(username: String, password: String, email: String? = null): Result<User> {
        return try {
            // 检查用户名是否已存在
            val existingUser = userDao.getUserByUsername(username)
            if (existingUser != null) {
                return Result.failure(Exception("用户名已存在"))
            }
            
            // 验证用户名格式
            if (!User.isValidUsername(username)) {
                return Result.failure(Exception("用户名格式不正确（3-20字符，仅限字母数字下划线和中文）"))
            }
            
            // 验证密码强度
            if (!User.isValidPassword(password)) {
                return Result.failure(Exception("密码至少需要6位"))
            }
            
            // 验证邮箱格式
            if (email != null && !User.isValidEmail(email)) {
                return Result.failure(Exception("邮箱格式不正确"))
            }
            
            // 加密密码
            val passwordHash = PasswordUtils.hashPassword(password)
            
            // 创建用户
            val user = User(
                username = username,
                passwordHash = passwordHash,
                email = email,
                displayName = username
            )
            
            val userId = userDao.insertUser(user)
            val createdUser = user.copy(id = userId)
            
            Result.success(createdUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * 用户登录
     */
    suspend fun login(username: String, password: String): Result<User> {
        return try {
            val user = userDao.getUserByUsername(username)
                ?: return Result.failure(Exception("用户名或密码错误"))
            
            // 验证密码
            if (!PasswordUtils.verifyPassword(password, user.passwordHash)) {
                return Result.failure(Exception("用户名或密码错误"))
            }
            
            // 保存登录状态
            saveLoginState(user.id, username)
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * 用户登出
     */
    fun logout() {
        prefs.edit().apply {
            remove(KEY_USER_ID)
            putBoolean(KEY_IS_LOGGED_IN, false)
            remove(KEY_USERNAME)
            apply()
        }
    }
    
    /**
     * 检查是否已登录
     */
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    /**
     * 获取当前用户ID
     */
    fun getCurrentUserId(): Long? {
        val userId = prefs.getLong(KEY_USER_ID, -1L)
        return if (userId == -1L) null else userId
    }
    
    /**
     * 获取当前用户名
     */
    fun getCurrentUsername(): String? {
        return prefs.getString(KEY_USERNAME, null)
    }
    
    /**
     * 获取当前用户信息
     */
    suspend fun getCurrentUser(): User? {
        val userId = getCurrentUserId() ?: return null
        return userDao.getUserById(userId)
    }
    
    /**
     * 获取当前用户信息（Flow）
     */
    fun getCurrentUserFlow(): Flow<User?>? {
        val userId = getCurrentUserId() ?: return null
        return userDao.getUserByIdFlow(userId)
    }
    
    /**
     * 保存登录状态
     */
    private fun saveLoginState(userId: Long, username: String) {
        prefs.edit().apply {
            putLong(KEY_USER_ID, userId)
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_USERNAME, username)
            apply()
        }
    }
    
    /**
     * 注册后保存登录状态（公共方法）
     */
    fun saveLoginStateAfterRegister(userId: Long, username: String) {
        saveLoginState(userId, username)
    }
    
    /**
     * 更新用户信息
     */
    suspend fun updateUser(user: User): Result<Unit> {
        return try {
            userDao.updateUser(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * 修改密码
     */
    suspend fun changePassword(userId: Long, oldPassword: String, newPassword: String): Result<Unit> {
        return try {
            val user = userDao.getUserById(userId)
                ?: return Result.failure(Exception("用户不存在"))
            
            // 验证旧密码
            if (!PasswordUtils.verifyPassword(oldPassword, user.passwordHash)) {
                return Result.failure(Exception("旧密码错误"))
            }
            
            // 验证新密码
            if (!User.isValidPassword(newPassword)) {
                return Result.failure(Exception("新密码至少需要6位"))
            }
            
            // 更新密码
            val newPasswordHash = PasswordUtils.hashPassword(newPassword)
            val updatedUser = user.copy(passwordHash = newPasswordHash)
            userDao.updateUser(updatedUser)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
