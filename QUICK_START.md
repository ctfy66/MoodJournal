# 快速开始指南 - MoodJournal 认证系统

## 🚀 5 分钟快速上手

### 第一步：同步项目

```
Android Studio > File > Sync Project with Gradle Files
等待同步完成
```

### 第二步：运行应用

```
连接设备或启动模拟器
点击 Run 按钮（绿色三角形）
```

### 第三步：注册账号

```
1. 应用启动后会显示欢迎界面
2. 自动跳转到登录页面
3. 点击 "立即注册"
4. 填写信息：
   - 用户名：3-20个字符（支持中文）
   - 密码：至少6位
   - 确认密码：与密码相同
   - 邮箱：可选
5. 点击 "注册" 按钮
```

### 第四步：登录

```
1. 注册成功后自动返回登录页
2. 输入用户名和密码
3. 点击 "登录" 按钮
4. 成功后进入主页
```

### 第五步：开始使用

```
✅ 记录心情：点击 "+" 按钮
✅ 查看历史：在主页查看记录列表
✅ 查看统计：切换到统计页面
✅ 管理账户：进入设置页面
```

---

## 📝 核心功能速览

### 用户认证

```kotlin
// 注册
authViewModel.register(username, password, confirmPassword, email)

// 登录
authViewModel.login(username, password)

// 登出
authViewModel.logout()

// 检查登录状态
val isLoggedIn = authViewModel.isLoggedIn.collectAsState()
```

### 心情记录

```kotlin
// 设置当前用户
moodViewModel.setUserId(userId)

// 插入记录
moodViewModel.insertEntry(moodLevel, factors, note)

// 获取记录
val allEntries = moodViewModel.allEntries.collectAsState()
```

---

## 🎯 常见使用场景

### 场景 1：用户首次使用

```
欢迎界面 → 登录页 → 注册页 → 登录页 → 主页
```

### 场景 2：用户再次打开应用

```
欢迎界面 → 主页（自动登录）
```

### 场景 3：切换账号

```
设置 → 退出登录 → 登录页 → 输入其他账号 → 主页
```

### 场景 4：记录心情

```
主页 → 点击 + → 选择情绪 → 选择因素 → 写笔记 → 提交 → 成功页 → 主页
```

---

## 🔐 测试账号示例

创建几个测试账号来测试数据隔离：

**账号 1**:

- 用户名: 小明
- 密码: 123456

**账号 2**:

- 用户名: 小红
- 密码: 123456

**账号 3**:

- 用户名: testuser
- 密码: password123

验证：每个账号的心情记录完全独立，互不影响。

---

## 🎨 页面导航

```
📱 应用结构
│
├─ 🌟 WelcomeScreen (欢迎页)
│
├─ 🔐 LoginScreen (登录页)
│   └─ 链接到 RegisterScreen
│
├─ 📝 RegisterScreen (注册页)
│   └─ 返回 LoginScreen
│
├─ 🏠 DashboardScreen (主页)
│   ├─ 显示最近记录
│   ├─ 显示统计卡片
│   └─ 导航按钮
│
├─ ✍️ MoodJournalScreen (记录心情)
│   └─ 完成后 → LogSuccessScreen
│
├─ 📜 HistoryScreen (历史记录)
│   └─ 显示所有记录列表
│
├─ 📊 StatsScreen (统计页面)
│   └─ 显示图表和分析
│
└─ ⚙️ SettingsScreen (设置)
    └─ 退出登录功能
```

---

## 💻 代码示例

### 在 Composable 中使用认证

```kotlin
@Composable
fun MyScreen() {
    val authViewModel: AuthViewModel = viewModel()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    if (isLoggedIn) {
        Text("欢迎，${currentUser?.username}")
    } else {
        Text("请先登录")
    }
}
```

### 在 Composable 中记录心情

```kotlin
@Composable
fun MoodEntryScreen() {
    val moodViewModel: MoodViewModel = viewModel()

    Button(onClick = {
        moodViewModel.insertEntry(
            moodLevel = 4, // 😄
            factors = listOf("工作", "睡眠"),
            note = "今天心情很好！"
        )
    }) {
        Text("提交心情")
    }
}
```

---

## 🔧 开发者工具

### 查看数据库

```
Android Studio > View > Tool Windows > Device File Explorer
导航到: /data/data/com.example.moodjournal/databases/
下载: mood_database
使用 DB Browser for SQLite 打开查看
```

### 清除应用数据

```
设置 > 应用 > MoodJournal > 存储 > 清除数据
或者：卸载重装应用
```

### 查看日志

```
Android Studio > Logcat
过滤: com.example.moodjournal
```

---

## ⚠️ 注意事项

### 数据库版本升级

- ⚠️ 从版本 1 升级到版本 2 会清空旧数据
- ✅ 这是正常行为（开发阶段）
- ✅ 生产环境需实现数据迁移

### 密码安全

- ✅ 当前使用 SHA-256 加密
- ⚠️ 生产环境建议升级到 BCrypt
- ⚠️ 建议添加密码盐值

### 会话管理

- ✅ 使用 SharedPreferences 存储
- ✅ 重启应用保持登录
- ⚠️ 建议添加会话过期机制

---

## 📱 UI 截图对照

### 登录流程

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   欢迎界面   │ --> │   登录界面   │ --> │   主页面    │
│     💙      │     │  用户名     │     │   Dashboard │
│  心情日记    │     │  密码       │     │             │
│             │     │ [登录按钮]   │     │  最近记录   │
└─────────────┘     └─────────────┘     └─────────────┘
                           ↓
                    ┌─────────────┐
                    │   注册界面   │
                    │  新用户信息  │
                    │ [注册按钮]   │
                    └─────────────┘
```

---

## 🎉 成功标志

运行成功的标志：

- ✅ 应用启动无错误
- ✅ 能够注册新用户
- ✅ 能够登录系统
- ✅ 能够记录心情
- ✅ 数据持久化保存
- ✅ 不同用户数据隔离
- ✅ 能够退出登录
- ✅ 重启应用保持登录状态

---

## 📚 更多资源

- **详细文档**: `AUTH_IMPLEMENTATION.md`
- **实现状态**: `IMPLEMENTATION_STATUS.md`
- **项目结构**: 查看 `app/src/main/java/` 目录

---

## 🆘 获取帮助

遇到问题？检查：

1. ✅ Gradle 是否同步成功
2. ✅ 所有文件是否正确创建
3. ✅ 数据库版本是否正确
4. ✅ 是否清理了旧数据

祝你使用愉快！💙
