# Bug 修复：注册新用户后显示旧用户数据

## 🐛 问题描述

**症状**: 退出登录后注册新用户时，记录显示的是上一个用户的数据。只有退出登录再登录进去，才正常显示0条记录。

**原因分析**:
1. 当用户登出时，`AuthViewModel` 的 `_currentUser` 被设置为 `null`
2. 但是 `MoodViewModel` 中的数据（`_allEntries`, `_recentEntries` 等）没有被清除
3. 注册新用户后跳转到登录页，此时 `currentUser` 仍然是 `null`
4. `LaunchedEffect(currentUser)` 检测到 `currentUser` 为 `null`，但没有清除 MoodViewModel 的数据
5. 所以旧用户的数据仍然显示在 UI 上
6. 只有重新登录时，`currentUser` 才会更新，触发 `setUserId`，加载新用户的数据

---

## ✅ 解决方案

### 1. 添加 `clearData()` 方法到 MoodViewModel

在 `MoodViewModel` 中添加清除所有数据的方法：

```kotlin
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
```

### 2. 在 MainActivity 中调用 clearData()

修改 `LaunchedEffect(currentUser)` 逻辑：

```kotlin
LaunchedEffect(currentUser) {
    currentUser?.let { user ->
        println("MainActivity: Current user changed to ${user.username} (id=${user.id})")
        moodViewModel.setUserId(user.id)
        println("MainActivity: MoodViewModel userId set to ${user.id}")
    } ?: run {
        println("MainActivity: Current user is null, clearing MoodViewModel data")
        moodViewModel.clearData()  // 当用户为 null 时清除数据
    }
}
```

### 3. 在注册成功后重置 authState

确保注册成功后不会保留旧的认证状态：

```kotlin
onRegisterSuccess = { 
    authViewModel.resetAuthState()
    currentScreen = "login"
}
```

### 4. 添加调试日志

在关键位置添加日志，方便追踪问题：

- `AuthViewModel.logout()` - 记录登出操作
- `AuthViewModel.resetAuthState()` - 记录重置状态
- `MoodViewModel.clearData()` - 记录数据清除

---

## 🔄 现在的完整流程

### 用户登出流程
```
1. 点击"退出登录" 
2. AuthViewModel.logout() 被调用
   - authRepository.logout() 清除 SharedPreferences
   - _currentUser.value = null
   - _isLoggedIn.value = false
3. LaunchedEffect(currentUser) 检测到 currentUser 变为 null
4. 调用 moodViewModel.clearData()
5. 清除所有心情记录数据
6. 跳转到登录页
```

### 注册新用户流程
```
1. 填写注册表单
2. AuthViewModel.register() 被调用
3. 注册成功但不设置 _currentUser（不自动登录）
4. _authState 设置为 Success
5. onRegisterSuccess 被触发
6. resetAuthState() 重置状态为 Idle
7. 跳转到登录页
8. 此时 currentUser 为 null，MoodViewModel 数据已清空
9. 用户手动登录
10. currentUser 更新为新用户
11. LaunchedEffect 触发，调用 setUserId()
12. 加载新用户的数据（应该是空的）
```

---

## ✨ 改进点

1. **自动清除数据**: 登出或 currentUser 变为 null 时自动清除
2. **不自动登录**: 注册成功后不自动登录，避免状态混乱
3. **完整的日志**: 添加详细日志便于调试
4. **状态重置**: 确保在适当的时机重置认证状态

---

## 🧪 测试步骤

请按以下步骤测试修复效果：

### 测试场景 1: 登出并注册新用户
```
1. 登录用户A，记录几条心情
2. 查看主页，确认显示用户A的记录
3. 退出登录
4. 注册新用户B
5. 登录用户B
6. ✅ 检查：主页应该显示0条记录（不是用户A的数据）
```

### 测试场景 2: 多次切换用户
```
1. 登录用户A，记录心情
2. 退出登录
3. 登录用户B，记录心情
4. 退出登录
5. 登录用户A
6. ✅ 检查：只显示用户A的记录
7. 退出登录
8. 登录用户B
9. ✅ 检查：只显示用户B的记录
```

### 测试场景 3: 注册后直接登录
```
1. 注册新用户C
2. 在注册成功页面，跳转到登录页
3. ✅ 检查：登录页面干净，没有旧数据残留
4. 登录用户C
5. ✅ 检查：主页显示0条记录
6. 记录一条心情
7. ✅ 检查：主页显示1条记录
```

---

## 📊 预期的 Logcat 输出

正常流程应该看到以下日志：

```
// 登出时
AuthViewModel: logout() called
AuthViewModel: User logged out, currentUser=null, isLoggedIn=false
MainActivity: Current user is null, clearing MoodViewModel data
MoodViewModel: Clearing all data

// 注册时
AuthViewModel: Registration successful for user: newuser

// 注册成功跳转
AuthViewModel: resetAuthState() called

// 登录时
MainActivity: Current user changed to newuser (id=2)
MoodViewModel: setUserId called with userId=2, current=null
MoodViewModel: UserId updated to 2, loading user data...
DashboardScreen: recentEntries.size=0, entryCount=0
```

---

## 💡 如果问题仍然存在

如果问题仍然出现，请检查：

1. **是否卸载重装了应用**
   - 旧版本的数据库可能有缓存

2. **查看 Logcat 日志**
   - 确认 `clearData()` 被调用
   - 确认 `setUserId()` 使用了正确的 userId

3. **检查 Database Inspector**
   - View > Tool Windows > App Inspection
   - 确认不同用户的数据确实独立

4. **强制停止应用**
   - Settings > Apps > MoodJournal > Force Stop
   - 重新打开应用测试

---

## 🎉 总结

此 bug 已修复！核心改进：

✅ 添加了 `clearData()` 方法清除旧数据
✅ 登出时自动清除 MoodViewModel 数据
✅ 注册成功后不自动登录，避免状态混乱
✅ 添加了完整的调试日志
✅ 确保数据隔离的完整性

现在注册新用户后应该看到干净的界面，不会显示旧用户的数据！
