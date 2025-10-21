# 调试指南 - 心情记录不显示问题

## 问题排查步骤

### 1. 检查日志输出

运行应用后，在 Android Studio 的 Logcat 中查找以下日志：

```
MoodViewModel: setUserId called with userId=X
MoodViewModel: UserId updated to X, loading user data...
MoodViewModel: Inserting entry for userId=X, moodLevel=X
MoodViewModel: Entry inserted successfully with id=X
```

### 2. 可能的问题和解决方案

#### 问题 1: userId 为 null

**症状**: 看到日志 "Cannot insert entry - userId is null"

**原因**: MoodViewModel 的 userId 没有正确设置

**解决方案**:

1. 确保用户已登录
2. 检查 MainActivity 中的 LaunchedEffect 是否被触发
3. 添加调试日志确认 currentUser 不为 null

#### 问题 2: 数据插入失败

**症状**: 看到错误日志或没有看到 "Entry inserted successfully"

**原因**: 数据库操作失败

**解决方案**:

1. 检查数据库版本是否正确（应该是 version 2）
2. 卸载应用重新安装（清除旧数据库）
3. 检查 userId 字段是否正确传递

#### 问题 3: 数据插入成功但不显示

**症状**: 看到 "Entry inserted successfully" 但 UI 不显示

**原因**: 数据加载或 UI 更新问题

**解决方案**:

1. 检查查询是否正确过滤 userId
2. 确认 StateFlow 正确更新
3. 重启应用查看数据是否持久化

### 3. 快速测试方法

#### 方法 1: 手动触发数据加载

在 DashboardScreen 添加刷新按钮：

```kotlin
Button(onClick = {
    authViewModel.currentUser.value?.let { user ->
        moodViewModel.setUserId(user.id)
    }
}) {
    Text("刷新数据")
}
```

#### 方法 2: 检查数据库

使用 Android Studio 的 Database Inspector:

1. View > Tool Windows > App Inspection
2. 选择 Database Inspector
3. 查看 mood_entries 表
4. 确认数据是否被插入

### 4. 常见解决方案

#### 方案 1: 重新初始化（最简单）

```
1. 卸载应用
2. 重新安装
3. 创建新账号
4. 测试记录心情
```

#### 方案 2: 强制重新加载数据

修改 MainActivity，在登录成功后延迟设置 userId:

```kotlin
LaunchedEffect(currentUser) {
    currentUser?.let { user ->
        delay(500) // 等待数据库初始化
        moodViewModel.setUserId(user.id)
    }
}
```

#### 方案 3: 检查协程作用域

确保所有数据库操作都在正确的协程作用域中执行

### 5. 验证步骤

完成修复后，按以下顺序测试：

1. ✅ 启动应用，检查日志是否显示 "setUserId called"
2. ✅ 登录账号，检查日志是否显示 userId
3. ✅ 记录一条心情，检查日志是否显示 "Entry inserted successfully"
4. ✅ 返回主页，查看是否显示新记录
5. ✅ 关闭重新打开应用，查看数据是否持久化
6. ✅ 登出并登录其他账号，验证数据隔离

### 6. 如果问题仍然存在

请提供以下信息：

1. Logcat 的完整日志
2. Database Inspector 中的数据截图
3. 具体的操作步骤
4. 是否有任何错误提示
