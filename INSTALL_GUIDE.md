# MoodJournal APK 安装指南

## 📱 APK 文件位置

✅ **APK 已成功生成！**

文件位置：`app\build\outputs\apk\debug\app-debug.apk`

文件大小：约 10 MB

## 🚀 安装方法

### 方法一：通过 USB 连接安装（推荐）

**前提条件：**
- Android 手机开启开发者模式和 USB 调试
- 电脑已安装 Android SDK（通常随 Android Studio 一起安装）

**步骤：**

1. **连接手机到电脑**
   - 使用 USB 数据线连接手机
   - 手机上允许 USB 调试授权

2. **使用 ADB 安装**
   ```powershell
   # 在项目根目录执行
   adb install app\build\outputs\apk\debug\app-debug.apk
   ```

3. **或者使用 Gradle 直接安装并运行**
   ```powershell
   .\gradlew installDebug
   ```

### 方法二：直接传输到手机安装

**步骤：**

1. **传输 APK 文件**
   - 将 `app-debug.apk` 复制到手机
   - 可以通过：
     - USB 传输
     - 微信/QQ 发送给自己
     - 云盘（百度网盘、OneDrive等）
     - 邮箱发送

2. **允许安装未知来源应用**
   - Android 8.0+：设置 → 安全 → 允许此来源（安装时会提示）
   - Android 8.0-：设置 → 安全 → 未知来源 → 开启

3. **安装 APK**
   - 在手机上找到 APK 文件
   - 点击安装
   - 允许所需权限

### 方法三：使用 Android Studio 直接运行

1. 打开 Android Studio
2. 连接手机或启动模拟器
3. 点击绿色运行按钮（▶️）或按 `Shift + F10`
4. 应用会自动安装并启动

## 📋 系统要求

- **最低 Android 版本**：Android 8.0 (API 26)
- **推荐 Android 版本**：Android 10.0+
- **存储空间**：至少 50 MB 可用空间
- **RAM**：建议 2GB 以上

## ⚠️ 重要提示

### Debug 版本说明
- ✅ 当前生成的是 **Debug 版本**（app-debug.apk）
- ✅ 适合测试和开发使用
- ❌ 不适合发布到应用商店
- ❌ 没有代码混淆和优化
- ❌ 包含调试信息，体积较大

### Release 版本（正式版）

如需生成正式发布版本，需要：

1. **创建签名密钥**
   ```powershell
   keytool -genkey -v -keystore my-release-key.keystore -alias my-key-alias -keyalg RSA -keysize 2048 -validity 10000
   ```

2. **配置签名信息**
   在 `app/build.gradle.kts` 中添加：
   ```kotlin
   signingConfigs {
       create("release") {
           storeFile = file("path/to/my-release-key.keystore")
           storePassword = "your-password"
           keyAlias = "my-key-alias"
           keyPassword = "your-password"
       }
   }
   ```

3. **构建 Release APK**
   ```powershell
   .\gradlew assembleRelease
   ```

4. **APK 位置**
   `app\build\outputs\apk\release\app-release.apk`

## 🔧 构建其他版本

### 生成 AAB（Android App Bundle）
用于 Google Play 发布：
```powershell
.\gradlew bundleRelease
```
输出位置：`app\build\outputs\bundle\release\app-release.aab`

### 列出所有构建任务
```powershell
.\gradlew tasks
```

### 清理构建
```powershell
.\gradlew clean
```

## 🐛 常见问题

### 1. 安装失败："解析软件包时出现问题"
- **原因**：APK 损坏或不兼容
- **解决**：重新构建 APK，检查 Android 版本

### 2. "未安装应用程序"
- **原因**：已安装不同签名的同名应用
- **解决**：先卸载旧版本再安装

### 3. ADB 无法识别设备
- **检查**：USB 调试是否开启
- **尝试**：更换 USB 线或端口
- **命令**：`adb devices` 查看设备列表

### 4. 权限问题
- **数据库权限**：应用首次运行时会自动创建
- **存储权限**：在应用设置中手动授权

## 📦 APK 信息

- **应用名称**：情绪日记 (MoodJournal)
- **包名**：com.example.moodjournal
- **版本名称**：1.0.0
- **版本号**：1
- **目标 SDK**：Android 34
- **最小 SDK**：Android 26

## 🎯 功能检查列表

安装后请测试以下功能：

- [ ] 用户注册和登录
- [ ] 添加心情记录
- [ ] 查看历史记录和统计
- [ ] 使用 CBT 功能
- [ ] 保存思维记录
- [ ] 退出登录
- [ ] 清除数据

## 📞 获取帮助

如遇到问题，可以：
1. 查看应用日志：`adb logcat | findstr MoodJournal`
2. 检查 GitHub Issues
3. 联系开发者

## 🔐 安全提示

- Debug 版本包含调试信息，不要用于生产环境
- 保护好你的签名密钥（Release 版本）
- 不要分享包含密钥的配置文件
- 建议使用 Android Keystore 系统

---

**构建日期**：2025-10-21  
**项目地址**：https://github.com/ctfy66/MoodJournal
