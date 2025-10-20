# 💙 MoodJournal - 心情日记

一个美观、实用的 Android 心情记录应用，帮助你追踪和分析每天的情绪变化。

## ✨ 特性

### 核心功能
- 📝 **心情记录** - 记录每天的情绪状态（5种情绪等级）
- 🎯 **因素标记** - 标记影响心情的因素（工作、睡眠、健康等）
- 📖 **日记笔记** - 添加文字记录详细的心情描述
- 📊 **数据统计** - 查看心情趋势、分布和连续记录天数
- 📜 **历史记录** - 浏览所有历史心情记录

### 用户系统
- 🔐 **用户注册/登录** - 完整的账号系统
- 👤 **多用户支持** - 每个用户的数据完全隔离
- 🔒 **密码加密** - SHA-256 加密保护用户密码
- 💾 **自动登录** - 记住登录状态，重启应用无需重新登录
- 🚪 **安全登出** - 随时退出账号

### 数据管理
- 💾 **本地存储** - 使用 Room 数据库持久化存储
- 🔄 **实时同步** - UI 自动更新最新数据
- 🗑️ **数据清除** - 可以清除所有记录

## 🎨 界面设计

- 🌙 **深色主题** - 护眼舒适的深色界面
- ✨ **现代化 UI** - Material Design 3 设计语言
- 💎 **流畅动画** - 丝滑的交互体验
- 📱 **响应式布局** - 适配不同屏幕尺寸

## 🏗️ 技术栈

### Android 开发
- **Kotlin** - 主要编程语言
- **Jetpack Compose** - 声明式 UI 框架
- **Material Design 3** - UI 设计系统

### 架构组件
- **MVVM 架构** - 清晰的代码结构
- **ViewModel** - UI 状态管理
- **StateFlow** - 响应式数据流
- **Coroutines** - 异步操作

### 数据存储
- **Room Database** - SQLite 数据库抽象层
- **SharedPreferences** - 轻量级键值存储
- **Gson** - JSON 序列化

## 📦 项目结构

```
app/src/main/java/com/example/moodjournal/
├── auth/                    # 认证相关页面
│   ├── WelcomeScreen.kt    # 欢迎页面
│   ├── LoginScreen.kt      # 登录页面
│   └── RegisterScreen.kt   # 注册页面
├── data/                    # 数据层
│   ├── User.kt             # 用户实体
│   ├── UserDao.kt          # 用户数据访问
│   ├── MoodEntry.kt        # 心情记录实体
│   ├── MoodEntryDao.kt     # 心情数据访问
│   ├── MoodDatabase.kt     # 数据库配置
│   ├── AuthRepository.kt   # 认证仓库
│   └── MoodRepository.kt   # 心情数据仓库
├── viewmodel/              # 视图模型层
│   ├── AuthViewModel.kt    # 认证状态管理
│   └── MoodViewModel.kt    # 心情数据管理
├── utils/                  # 工具类
│   └── PasswordUtils.kt    # 密码加密工具
├── ui/theme/               # UI 主题
├── MainActivity.kt         # 主活动
├── MoodJournalScreen.kt   # 记录心情页面
├── DashboardScreen.kt      # 主页面
├── HistoryScreen.kt        # 历史记录页面
├── StatsScreen.kt          # 统计页面
├── SettingsScreen.kt       # 设置页面
└── LogSuccessScreen.kt     # 成功提示页面
```

## 🚀 开始使用

### 环境要求
- Android Studio Hedgehog | 2023.1.1 或更高版本
- Android SDK 24 或更高版本
- Kotlin 2.0+

### 安装步骤

1. **克隆仓库**
   ```bash
   git clone git@github.com:ctfy66/MoodJournal.git
   cd MoodJournal
   ```

2. **打开项目**
   - 使用 Android Studio 打开项目
   - 等待 Gradle 同步完成

3. **运行应用**
   - 连接 Android 设备或启动模拟器
   - 点击 Run 按钮 (▶️)

## 📱 功能截图

### 用户认证
- 欢迎页面 - 应用启动界面
- 登录页面 - 用户登录
- 注册页面 - 新用户注册

### 主要功能
- 主页面 - 展示最近记录和统计卡片
- 记录心情 - 选择情绪、因素和写笔记
- 历史记录 - 查看所有历史记录
- 统计分析 - 查看心情趋势和分析
- 设置页面 - 账户管理和数据清除

## 🔐 安全性

- ✅ 密码使用 SHA-256 哈希加密
- ✅ 不存储明文密码
- ✅ 私有模式 SharedPreferences
- ✅ SQL 注入防护（Room 自动处理）
- ✅ 完整的输入验证

**⚠️ 注意**: 当前版本适用于学习和开发。生产环境建议：
- 使用 BCrypt 或 Argon2 替代 SHA-256
- 添加密码盐值（salt）
- 实现会话过期机制
- 添加双因素认证

## 📝 开发日志

### v1.0.0 (MVP) - 2025-10-20
- ✅ 完整的用户认证系统
- ✅ 心情记录功能
- ✅ 历史记录查看
- ✅ 数据统计分析
- ✅ 多用户数据隔离
- ✅ 本地数据持久化

## 🛣️ Roadmap

### 近期计划
- [ ] 密码找回功能
- [ ] 用户头像上传
- [ ] 数据导出功能
- [ ] 备份到云端
- [ ] 生物识别登录
- [ ] 密码强度增强（BCrypt）

### 未来展望
- [ ] 心情提醒通知
- [ ] 更多统计图表
- [ ] 心情日历视图
- [ ] 标签和分类系统
- [ ] 第三方登录（Google、微信）
- [ ] 多设备同步

## 🤝 贡献

欢迎贡献代码、报告问题或提出建议！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 👤 作者

**ctfy66**
- GitHub: [@ctfy66](https://github.com/ctfy66)

## 🙏 致谢

- [Material Design 3](https://m3.material.io/) - UI 设计指南
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - UI 框架
- [Room](https://developer.android.com/training/data-storage/room) - 数据库解决方案

## 📬 联系方式

如有问题或建议，请：
- 提交 [Issue](https://github.com/ctfy66/MoodJournal/issues)
- 发起 [Discussion](https://github.com/ctfy66/MoodJournal/discussions)

---

⭐ 如果这个项目对你有帮助，请给个 Star 支持一下！

💙 用心记录，感受生活
