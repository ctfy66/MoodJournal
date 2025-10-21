# GitHub Release 发布指南

## 🚀 快速发布（推荐）

### 使用 Debug APK 发布

Debug 版本适合个人使用和测试分享，无需签名配置。

#### 步骤 1：创建 Git Tag

```powershell
# 创建版本标签
git tag -a v1.0.0 -m "Release v1.0.0 - Initial MVP version"

# 推送标签到 GitHub
git push origin v1.0.0
```

#### 步骤 2：在 GitHub 网页创建 Release

1. 访问项目页面：https://github.com/ctfy66/MoodJournal
2. 点击右侧 "Releases" → "Create a new release"
3. 选择刚创建的标签：`v1.0.0`
4. 填写 Release 信息：

**Release Title（标题）：**
```
MoodJournal v1.0.0 - 情绪日记首个版本
```

**Description（描述）：**
```markdown
## 🎉 MoodJournal v1.0.0

### ✨ 核心功能
- ✅ 用户认证系统（注册/登录/登出）
- ✅ 情绪记录与追踪
- ✅ 日历视图展示心情历史
- ✅ 统计分析与趋势图表
- ✅ 认知行为疗法（CBT）工具
  - 10种认知扭曲识别
  - 思维记录工具
  - 7种挑战技巧
- ✅ 思维记录历史查看
- ✅ 多用户数据隔离
- ✅ 深色主题设计

### 📱 系统要求
- Android 8.0 (API 26) 或更高版本
- 至少 50 MB 可用空间

### 📥 下载安装
1. 下载 `app-debug.apk`
2. 在手机上安装（需允许"未知来源"）
3. 打开应用并注册账号

### 🔒 安全说明
这是 Debug 版本，仅用于测试和个人使用，不适合发布到应用商店。

### 📝 更新日志
- 初始版本发布
- 实现完整的情绪追踪功能
- 集成 CBT 认知行为疗法模块
- 支持多用户独立数据管理

### 🐛 已知问题
- 部分 Material Icons 使用旧版 API（有弃用警告）
- 通知提醒、深色模式切换等功能待实现

### 👨‍💻 技术栈
- Kotlin + Jetpack Compose
- Room Database
- Material Design 3
- MVVM Architecture

---
**发布日期**：2025-10-21  
**包名**：com.example.moodjournal  
**版本号**：1.0.0 (1)
```

5. **上传 APK 文件**
   - 点击"Attach binaries"
   - 选择 `app\build\outputs\apk\debug\app-debug.apk`
   - 可选：重命名为 `MoodJournal-v1.0.0-debug.apk`

6. **发布**
   - 勾选 "Set as the latest release"
   - 如果是测试版，勾选 "This is a pre-release"
   - 点击 "Publish release"

## 🔐 正式发布（Release 版本）

### 仅在需要发布到应用商店时使用

#### 步骤 1：创建签名密钥

```powershell
# 找到 keytool（通常在 JDK 的 bin 目录）
# Android Studio 自带的 JDK 路径：
# C:\Program Files\Android\Android Studio\jbr\bin\keytool.exe

keytool -genkey -v -keystore moodjournal-release-key.keystore -alias moodjournal -keyalg RSA -keysize 2048 -validity 10000
```

**需要设置的信息：**
- **密钥库口令**：设置一个强密码（例如：MoodJournal@2025）
- **确认密码**：再次输入相同密码
- **姓名**：输入你的名字
- **组织单位**：Development（或随意）
- **组织**：MoodJournal
- **城市**：Beijing（或你的城市）
- **省份**：Beijing（或你的省份）
- **国家代码**：CN（中国）
- **密钥口令**：直接按回车（使用与密钥库相同的密码）

**⚠️ 重要：**
- 妥善保管 `.keystore` 文件和密码
- 丢失后将无法更新应用！
- 不要上传到 GitHub

#### 步骤 2：配置签名

创建 `keystore.properties` 文件（不要提交到 Git）：

```properties
storePassword=你的密钥库密码
keyPassword=你的密钥密码
keyAlias=moodjournal
storeFile=../moodjournal-release-key.keystore
```

更新 `app/build.gradle.kts`：

```kotlin
// 在 android 块外部
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

android {
    // ... 其他配置
    
    signingConfigs {
        create("release") {
            if (keystorePropertiesFile.exists()) {
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
            }
        }
    }
    
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
```

更新 `.gitignore`：

```
*.keystore
keystore.properties
```

#### 步骤 3：构建 Release APK

```powershell
.\gradlew assembleRelease
```

输出位置：`app\build\outputs\apk\release\app-release.apk`

#### 步骤 4：发布到 GitHub

与 Debug 版本相同的步骤，但上传 `app-release.apk`。

## 📦 使用 GitHub CLI（可选）

如果安装了 GitHub CLI (`gh`)：

```powershell
# 创建 Release
gh release create v1.0.0 `
  app\build\outputs\apk\debug\app-debug.apk `
  --title "MoodJournal v1.0.0" `
  --notes-file RELEASE_NOTES.md

# 或直接输入描述
gh release create v1.0.0 `
  app\build\outputs\apk\debug\app-debug.apk `
  --title "MoodJournal v1.0.0" `
  --notes "Initial MVP release with mood tracking and CBT features"
```

## 🔄 更新发布

发布新版本：

```powershell
# 1. 更新版本号（在 app/build.gradle.kts 中）
versionCode = 2
versionName = "1.1.0"

# 2. 构建新 APK
.\gradlew assembleDebug  # 或 assembleRelease

# 3. 创建新标签
git tag -a v1.1.0 -m "Release v1.1.0"
git push origin v1.1.0

# 4. 在 GitHub 创建新 Release
```

## 📋 发布检查清单

发布前确认：

- [ ] 应用功能全部测试通过
- [ ] 版本号已更新
- [ ] 更新日志已准备
- [ ] APK 已成功构建
- [ ] Git 代码已推送
- [ ] 标签已创建
- [ ] README.md 已更新
- [ ] 截图已准备（可选）

## 🎯 建议的版本号规则

- **主版本号**：重大功能变更（1.0.0 → 2.0.0）
- **次版本号**：新功能添加（1.0.0 → 1.1.0）
- **修订号**：Bug 修复（1.0.0 → 1.0.1）

## 📸 添加截图（可选）

在 Release 描述中添加应用截图：

```markdown
## 📱 应用截图

![登录页面](screenshots/login.png)
![主页面](screenshots/dashboard.png)
![CBT 功能](screenshots/cbt.png)
```

---

**提示**：首次发布建议使用 Debug 版本，等功能完善后再发布正式的 Release 版本。
