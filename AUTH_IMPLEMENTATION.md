# 登入登出系统实现文档

## 📋 项目概述

已为 MoodJournal 应用成功实现了完整的用户认证系统，包括登录、注册、登出和数据隔离功能。

---

## 🏗️ 系统架构

### 1. 数据层 (Data Layer)

#### **User.kt** - 用户实体

```
位置: app/src/main/java/com/example/moodjournal/data/User.kt
功能:
- 存储用户信息（ID、用户名、密码哈希、邮箱等）
- 提供用户名、密码、邮箱验证规则
```

#### **UserDao.kt** - 用户数据访问接口

```
位置: app/src/main/java/com/example/moodjournal/data/UserDao.kt
功能:
- 插入、更新、删除用户
- 通过用户名或ID查询用户
- 统计用户数量
```

#### **AuthRepository.kt** - 认证仓库

```
位置: app/src/main/java/com/example/moodjournal/data/AuthRepository.kt
功能:
- 用户注册（包含验证和密码加密）
- 用户登录（密码验证）
- 用户登出
- 会话管理（SharedPreferences）
- 密码修改
```

#### **MoodDatabase.kt** - 数据库（已更新）

```
版本: 2
新增内容:
- 添加 User 表
- 添加 UserDao
- 支持数据库迁移
```

#### **MoodEntry.kt** - 心情记录实体（已更新）

```
新增字段: userId (Long)
作用: 实现数据隔离，每个用户只能看到自己的心情记录
```

#### **PasswordUtils.kt** - 密码工具类

```
位置: app/src/main/java/com/example/moodjournal/utils/PasswordUtils.kt
功能:
- SHA-256 密码加密
- 密码验证
- 密码强度检测（弱/中等/强）
```

---

### 2. 业务逻辑层 (Business Logic Layer)

#### **AuthViewModel.kt** - 认证视图模型

```
位置: app/src/main/java/com/example/moodjournal/viewmodel/AuthViewModel.kt

状态管理:
- authState: 认证状态（Idle/Loading/Success/Error）
- isLoggedIn: 登录状态
- currentUser: 当前用户信息

功能方法:
- login(username, password): 用户登录
- register(username, password, confirmPassword, email): 用户注册
- logout(): 用户登出
- checkLoginStatus(): 检查登录状态
- changePassword(): 修改密码
- updateUserInfo(): 更新用户信息
- getCurrentUserId(): 获取当前用户ID
```

#### **MoodViewModel.kt** - 心情视图模型（已更新）

```
位置: app/src/main/java/com/example/moodjournal/viewmodel/MoodViewModel.kt

更新内容:
- 添加 currentUserId 状态
- setUserId(userId): 设置当前用户并加载数据
- loadUserData(userId): 加载指定用户的所有数据
- 所有数据查询都基于 userId 进行过滤
```

#### **MoodRepository.kt** - 心情仓库（已更新）

```
更新内容:
- 所有方法都添加了 userId 参数
- getAllEntries(userId)
- getRecentEntries(userId, limit)
- getWeekEntries(userId)
- getMonthEntries(userId)
- deleteAllEntries(userId)
```

---

### 3. UI 层 (Presentation Layer)

#### **WelcomeScreen.kt** - 欢迎页面

```
位置: app/src/main/java/com/example/moodjournal/auth/WelcomeScreen.kt
功能:
- 应用启动时显示
- 检查登录状态
- 自动导航到登录页或主页
```

#### **LoginScreen.kt** - 登录页面

```
位置: app/src/main/java/com/example/moodjournal/auth/LoginScreen.kt
功能:
- 用户名密码登录
- 密码可见性切换
- 错误提示
- 跳转到注册页面
- 登录成功自动跳转
```

#### **RegisterScreen.kt** - 注册页面

```
位置: app/src/main/java/com/example/moodjournal/auth/RegisterScreen.kt
功能:
- 用户注册表单
- 实时密码强度提示
- 两次密码验证
- 邮箱输入（可选）
- 输入验证和错误提示
- 注册成功跳转到登录页
```

#### **SettingsScreen.kt** - 设置页面（已更新）

```
更新内容:
- 显示当前用户信息
- 退出登录按钮
- 退出确认对话框
- 清除数据确认对话框
```

#### **MainActivity.kt** - 主活动（已更新）

```
更新内容:
- 添加 authViewModel
- 新增认证相关页面路由
- welcome -> login/register -> dashboard
- 用户登录成功后自动设置 moodViewModel 的 userId
```

---

## 🔐 核心功能说明

### 1. 用户注册流程

```
用户输入 → 前端验证 → AuthViewModel.register() →
AuthRepository.register() →
  - 检查用户名是否存在
  - 验证用户名格式（3-20字符）
  - 验证密码强度（最少6位）
  - 验证邮箱格式（可选）
  - SHA-256 加密密码
  - 插入数据库
→ 返回成功 → 跳转登录页
```

### 2. 用户登录流程

```
用户输入 → AuthViewModel.login() →
AuthRepository.login() →
  - 查询用户
  - 验证密码
  - 保存登录状态到 SharedPreferences
→ 返回用户信息 →
MoodViewModel.setUserId() →
加载用户数据 → 跳转主页
```

### 3. 数据隔离机制

```
每个 MoodEntry 都包含 userId 字段
所有查询都通过 WHERE userId = ? 过滤
用户只能看到和操作自己的数据
统计数据也基于当前用户计算
```

### 4. 会话管理

```
使用 SharedPreferences 存储:
- current_user_id: 当前用户ID
- is_logged_in: 是否已登录
- username: 用户名

应用启动时自动检查登录状态
登出时清除所有会话信息
```

---

## 🎨 UI 设计特点

### 配色方案

- **深色背景**: #0A0C1E
- **卡片背景**: #1A1D35
- **边框颜色**: #2D3254
- **青色按钮**: #00E5FF
- **白色文本**: #FFFFFF
- **灰色文本**: #9CA3AF
- **错误红色**: #EF4444

### 交互设计

- **渐变背景**: 提供视觉深度
- **实时验证**: 输入时即时反馈
- **密码强度指示**: 三级强度显示（弱/中等/强）
- **加载状态**: 按钮显示加载动画
- **错误提示**: 清晰的错误信息展示
- **确认对话框**: 重要操作（登出、删除）需二次确认

---

## 📦 数据库变更

### 版本升级

- **旧版本**: 1
- **新版本**: 2

### 新增表: users

```sql
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL,
    passwordHash TEXT NOT NULL,
    email TEXT,
    createdAt INTEGER,
    displayName TEXT
)
```

### 更新表: mood_entries

```sql
ALTER TABLE mood_entries ADD COLUMN userId INTEGER NOT NULL
```

### 数据迁移

使用 `.fallbackToDestructiveMigration()` 策略
升级时会清空旧数据（适用于开发阶段）
生产环境建议实现 Migration 策略

---

## 🔒 安全性考虑

### 1. 密码安全

- ✅ 使用 SHA-256 哈希加密
- ✅ 不存储明文密码
- ✅ 密码最少 6 位限制
- ⚠️ 建议增强: 添加盐值(salt)和多轮哈希

### 2. 输入验证

- ✅ 用户名格式验证
- ✅ 密码强度检测
- ✅ 邮箱格式验证
- ✅ SQL 注入防护（Room 自动处理）

### 3. 会话管理

- ✅ SharedPreferences 私有模式
- ✅ 登出清除会话
- ⚠️ 建议增强: 添加会话过期机制

---

## 🚀 使用指南

### 首次运行

1. 启动应用，显示欢迎界面
2. 自动跳转到登录页面
3. 点击"立即注册"创建账号
4. 填写用户名（3-20 字符）
5. 设置密码（最少 6 位）
6. 可选填写邮箱
7. 注册成功后返回登录页
8. 输入用户名和密码登录
9. 进入应用主界面

### 日常使用

- 登录后自动记住状态
- 关闭应用后重新打开无需再次登录
- 每个用户的心情记录完全隔离
- 在设置页面可以退出登录

### 切换账号

1. 进入设置页面
2. 点击"退出登录"
3. 确认退出
4. 返回登录页面
5. 使用其他账号登录

---

## 📱 页面导航流程

```
启动
  ↓
WelcomeScreen（检测登录状态）
  ↓
├─已登录 → DashboardScreen（主页）
│              ↓
│          ├─ MoodJournalScreen（记录心情）
│          ├─ HistoryScreen（历史记录）
│          ├─ StatsScreen（统计）
│          └─ SettingsScreen（设置）
│                  ↓
│              退出登录 → LoginScreen
│
└─未登录 → LoginScreen（登录）
              ↓
          ├─ 登录成功 → DashboardScreen
          └─ 注册 → RegisterScreen
                      ↓
                  注册成功 → LoginScreen
```

---

## ⚙️ 配置要求

### Gradle 依赖（已配置）

```kotlin
// Room 数据库
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
ksp("androidx.room:room-compiler:2.6.1")

// ViewModel
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

// Gson（JSON序列化）
implementation("com.google.code.gson:gson:2.10.1")
```

### 最低要求

- minSdk: 24 (Android 7.0)
- targetSdk: 36
- Kotlin: 2.0+
- Compose: 最新版本

---

## 🧪 测试建议

### 功能测试

1. ✅ 用户注册 - 正常流程
2. ✅ 用户注册 - 重复用户名
3. ✅ 用户注册 - 无效输入
4. ✅ 用户登录 - 正确密码
5. ✅ 用户登录 - 错误密码
6. ✅ 数据隔离 - 多用户数据独立
7. ✅ 会话保持 - 重启应用保持登录
8. ✅ 退出登录 - 清除会话

### UI 测试

1. 表单验证实时反馈
2. 密码强度指示器
3. 加载状态显示
4. 错误提示清晰
5. 页面跳转流畅

---

## 🐛 已知问题

### 当前版本

- ⚠️ 数据库升级使用破坏性迁移（开发阶段可接受）
- ⚠️ 密码加密未使用盐值（建议增强）
- ⚠️ 无会话过期机制（建议添加）
- ⚠️ 无密码找回功能（计划添加）

---

## 🔄 未来优化建议

### 安全增强

1. **密码加密升级**

   - 使用 BCrypt 或 Argon2
   - 添加随机盐值
   - 多轮哈希迭代

2. **会话管理**

   - 添加会话过期时间
   - 实现刷新令牌机制
   - 异常登录检测

3. **数据安全**
   - 本地数据加密
   - 生物识别登录
   - 双因素认证

### 功能扩展

1. **密码管理**

   - 忘记密码/找回密码
   - 密码重置
   - 定期修改密码提醒

2. **用户体验**

   - 第三方登录（Google、微信等）
   - 自动登录选项
   - 记住用户名

3. **数据同步**

   - 云端备份
   - 多设备同步
   - 数据导出功能

4. **社交功能**
   - 用户头像上传
   - 个人资料编辑
   - 好友系统（可选）

---

## 📞 技术支持

如遇到问题，请检查：

1. Gradle 同步是否成功
2. 所有依赖是否正确安装
3. 数据库版本是否正确升级
4. SharedPreferences 权限是否正常

---

## ✅ 实现清单

- [x] User 数据模型
- [x] 密码加密工具
- [x] UserDao 数据访问
- [x] AuthRepository 认证逻辑
- [x] 数据库版本升级
- [x] MoodEntry 添加 userId
- [x] AuthViewModel 状态管理
- [x] MoodViewModel 用户隔离
- [x] MoodRepository 用户过滤
- [x] WelcomeScreen 欢迎页
- [x] LoginScreen 登录页
- [x] RegisterScreen 注册页
- [x] SettingsScreen 更新
- [x] MainActivity 导航集成
- [x] 数据隔离实现
- [x] 会话管理

---

## 🎉 总结

已成功为 MoodJournal 应用实现了完整的登入登出系统！

### 核心特性

✅ 用户注册和登录
✅ 密码加密存储
✅ 会话管理和自动登录
✅ 数据完全隔离
✅ 现代化 UI 设计
✅ 完整的错误处理
✅ 流畅的用户体验

现在你可以：

1. Sync Gradle 同步项目
2. Build 编译应用
3. Run 运行测试
4. 注册新用户开始使用！

祝你使用愉快！💙
