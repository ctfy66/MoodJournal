package com.example.moodjournal.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodjournal.data.AuthRepository
import com.example.moodjournal.data.MoodDatabase
import com.example.moodjournal.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = MoodDatabase.getDatabase(application)
    private val authRepository = AuthRepository(application, database.userDao())
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    init {
        checkLoginStatus()
    }
    
    /**
     * 检查登录状态
     */
    fun checkLoginStatus() {
        viewModelScope.launch {
            val loggedIn = authRepository.isLoggedIn()
            _isLoggedIn.value = loggedIn
            
            if (loggedIn) {
                val user = authRepository.getCurrentUser()
                _currentUser.value = user
                if (user != null) {
                    _authState.value = AuthState.Success(user)
                }
            }
        }
    }
    
    /**
     * 用户登录
     */
    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("用户名和密码不能为空")
            return
        }
        
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            val result = authRepository.login(username, password)
            
            if (result.isSuccess) {
                val user = result.getOrNull()!!
                _currentUser.value = user
                _isLoggedIn.value = true
                _authState.value = AuthState.Success(user)
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "登录失败"
                _authState.value = AuthState.Error(errorMessage)
            }
        }
    }
    
    /**
     * 用户注册
     */
    fun register(username: String, password: String, confirmPassword: String, email: String? = null) {
        // 验证输入
        if (username.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("用户名和密码不能为空")
            return
        }
        
        if (password != confirmPassword) {
            _authState.value = AuthState.Error("两次输入的密码不一致")
            return
        }
        
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            val result = authRepository.register(username, password, email)
            
            if (result.isSuccess) {
                val user = result.getOrNull()!!
                println("AuthViewModel: Registration successful for user: ${user.username} (id=${user.id})")
                
                // 注册成功后自动登录
                _currentUser.value = user
                _isLoggedIn.value = true
                _authState.value = AuthState.Success(user)
                
                // 保存登录状态
                authRepository.saveLoginStateAfterRegister(user.id, user.username)
                
                println("AuthViewModel: Auto-login after registration, userId=${user.id}")
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "注册失败"
                println("AuthViewModel: Registration failed: $errorMessage")
                _authState.value = AuthState.Error(errorMessage)
            }
        }
    }
    
    /**
     * 用户登出
     */
    fun logout() {
        println("AuthViewModel: logout() called")
        authRepository.logout()
        _currentUser.value = null
        _isLoggedIn.value = false
        _authState.value = AuthState.Idle
        println("AuthViewModel: User logged out, currentUser=null, isLoggedIn=false")
    }
    
    /**
     * 重置认证状态
     */
    fun resetAuthState() {
        println("AuthViewModel: resetAuthState() called")
        _authState.value = AuthState.Idle
    }
    
    /**
     * 获取当前用户ID
     */
    fun getCurrentUserId(): Long? {
        return _currentUser.value?.id ?: authRepository.getCurrentUserId()
    }
    
    /**
     * 修改密码
     */
    fun changePassword(oldPassword: String, newPassword: String, confirmPassword: String) {
        if (oldPassword.isBlank() || newPassword.isBlank()) {
            _authState.value = AuthState.Error("密码不能为空")
            return
        }
        
        if (newPassword != confirmPassword) {
            _authState.value = AuthState.Error("两次输入的新密码不一致")
            return
        }
        
        val userId = getCurrentUserId()
        if (userId == null) {
            _authState.value = AuthState.Error("用户未登录")
            return
        }
        
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            val result = authRepository.changePassword(userId, oldPassword, newPassword)
            
            if (result.isSuccess) {
                _authState.value = AuthState.Idle
                // 可以显示成功消息
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "修改密码失败"
                _authState.value = AuthState.Error(errorMessage)
            }
        }
    }
    
    /**
     * 更新用户信息
     */
    fun updateUserInfo(displayName: String?, email: String?) {
        val user = _currentUser.value ?: return
        
        viewModelScope.launch {
            val updatedUser = user.copy(
                displayName = displayName,
                email = email
            )
            
            val result = authRepository.updateUser(updatedUser)
            if (result.isSuccess) {
                _currentUser.value = updatedUser
            }
        }
    }
}
