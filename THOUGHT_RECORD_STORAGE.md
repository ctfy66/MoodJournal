# 思维记录功能完整实现说明

## 问题解答

### 1. 思维记录保存在哪了？

**答案**：思维记录现在保存在 Room 数据库中的 `thought_records` 表。

#### 数据库结构

```kotlin
@Entity(tableName = "thought_records")
data class ThoughtRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,              // 记录ID（自动生成）
    val userId: Long,              // 用户ID（关联用户）
    val situation: String,         // 情境
    val automaticThought: String,  // 自动化思维
    val emotion: String,           // 情绪反应
    val distortionType: String,    // 认知扭曲类型
    val evidence: String,          // 支持证据
    val alternativeThought: String, // 替代思维
    val timestamp: Long            // 时间戳
)
```

#### 存储位置

- **数据库文件**: `/data/data/com.example.moodjournal/databases/mood_database`
- **数据库版本**: Version 3（新增了 thought_records 表）
- **多用户隔离**: 每条记录通过 `userId` 字段关联到特定用户
- **持久化存储**: 使用 Room Database，数据永久保存

### 2. 怎么查看？

提供了完整的查看功能：

#### 方式一：在 CBT 页面查看统计

- 打开 CBT 页面，顶部显示已保存的思维记录数量
- 例如："已保存 5 条思维记录"
- 点击右上角"查看历史"按钮

#### 方式二：思维记录历史页面

**功能特点**：

- **列表视图** - 显示所有思维记录的卡片列表

  - 显示日期
  - 显示认知扭曲类型（彩色标签）
  - 显示情境和自动化思维的摘要（前 50 字）
  - 显示记录时间
  - 每条记录有删除按钮

- **详情视图** - 点击任意记录查看完整内容

  - 完整的 6 步思维分析
  - 情境、自动化思维、情绪、证据、替代思维
  - 更平衡的想法有特殊高亮显示（紫色背景）
  - 可以删除该记录

- **空状态提示** - 没有记录时显示友好提示
  - "还没有思维记录"
  - "开始记录和挑战负面思维吧"

### 3. 和一般的情绪记录有什么区别？

#### 情绪记录（MoodEntry）

**目的**: 快速记录日常情绪状态
**数据结构**:

```kotlin
data class MoodEntry(
    val id: Int,
    val userId: Int,
    val moodLevel: Int,        // 1-5 心情等级
    val factors: List<String>, // 影响因素（工作、关系等）
    val note: String,          // 简短笔记
    val timestamp: Long
)
```

**使用场景**:

- 每天快速记录心情
- 选择 1-5 级心情
- 选择影响因素
- 写简短笔记（可选）
- 用于统计和趋势分析

**展示方式**:

- 日历视图
- 统计图表
- 历史记录列表

#### 思维记录（ThoughtRecord）

**目的**: 深度分析和挑战负面思维模式（CBT 核心工具）
**数据结构**:

```kotlin
data class ThoughtRecord(
    val id: Long,
    val userId: Long,
    val situation: String,         // 触发情境（详细描述）
    val automaticThought: String,  // 自动化思维（负面想法）
    val emotion: String,           // 情绪和强度（如"焦虑 8/10"）
    val distortionType: String,    // 认知扭曲类型（全或无思维等）
    val evidence: String,          // 支持证据（客观事实）
    val alternativeThought: String, // 替代思维（更平衡的想法）
    val timestamp: Long
)
```

**使用场景**:

- 遇到负面情绪时的深度分析
- 识别认知扭曲模式
- 挑战不合理信念
- 培养更平衡的思维方式
- 心理治疗练习

**展示方式**:

- 完整的结构化记录
- 分步骤展示思维分析过程
- 高亮替代性思维
- 历史记录可追溯

#### 核心区别对比表

| 维度       | 情绪记录       | 思维记录         |
| ---------- | -------------- | ---------------- |
| **用途**   | 日常情绪追踪   | 认知行为治疗     |
| **频率**   | 每天记录       | 遇到问题时记录   |
| **时间**   | 1-2 分钟       | 10-15 分钟       |
| **深度**   | 简单快速       | 深度分析         |
| **内容**   | 心情+因素+笔记 | 6 步完整思维分析 |
| **目标**   | 了解情绪模式   | 改变思维模式     |
| **展示**   | 图表统计       | 结构化文本       |
| **心理学** | 情绪追踪       | CBT 疗法         |

#### 互补关系

两种记录互相补充，形成完整的心理健康管理系统：

- **情绪记录** - 宏观视角，看整体趋势
- **思维记录** - 微观视角，深入分析具体问题

用户可以：

1. 每天用情绪记录追踪心情
2. 发现情绪低落的模式
3. 使用思维记录深度分析特定负面事件
4. 通过 CBT 技巧改变思维模式
5. 长期改善整体心理健康

## 新增功能

### 数据层

1. **ThoughtRecord.kt** - 思维记录实体
2. **ThoughtRecordDao.kt** - 数据访问对象
   - 查询所有记录
   - 查询最近记录
   - 插入记录
   - 删除记录
   - 统计数量
3. **ThoughtRecordRepository.kt** - 仓库层
4. **MoodDatabase.kt** - 升级到 Version 3，添加 thought_records 表

### ViewModel 层

**ThoughtRecordViewModel.kt** - 状态管理

- `setUserId()` - 设置当前用户
- `clearData()` - 清空数据
- `insertThoughtRecord()` - 插入新记录
- `deleteThoughtRecord()` - 删除记录
- `allThoughtRecords` - 所有记录的 StateFlow
- `thoughtRecordCount` - 记录数量的 StateFlow
- 格式化时间戳的工具方法

### UI 层

1. **CBTScreen.kt** - 更新

   - 集成 ThoughtRecordViewModel
   - 显示记录数量统计
   - "查看历史"按钮
   - 保存功能连接到数据库

2. **ThoughtRecordHistoryScreen.kt** - 新增（400+ 行）

   - 列表视图展示所有记录
   - 点击查看详情
   - 删除功能（带确认对话框）
   - 空状态提示
   - 精美的卡片设计

3. **MainActivity.kt** - 更新
   - 集成 ThoughtRecordViewModel
   - 添加 "thought_history" 路由
   - 用户切换时同步 userId
   - 登出时清空数据

## 数据流程

### 保存流程

```
用户填写表单
    ↓
点击"保存思维记录"按钮
    ↓
CBTScreen 调用 viewModel.insertThoughtRecord()
    ↓
ThoughtRecordViewModel 创建 ThoughtRecord 对象
    ↓
Repository 调用 DAO
    ↓
Room Database 保存到 thought_records 表
    ↓
StateFlow 更新，UI 自动刷新
    ↓
显示成功消息，表单清空
```

### 查看流程

```
CBT 页面点击"查看历史"
    ↓
导航到 ThoughtRecordHistoryScreen
    ↓
ViewModel 从数据库加载记录（通过 userId 过滤）
    ↓
StateFlow 发送数据到 UI
    ↓
显示记录列表
    ↓
点击卡片 → 显示详情视图
```

### 用户隔离

```
用户登录
    ↓
AuthViewModel 设置 currentUser
    ↓
MainActivity LaunchedEffect 监听 currentUser 变化
    ↓
调用 thoughtRecordViewModel.setUserId(user.id)
    ↓
ViewModel 使用 userId 查询数据库
    ↓
只加载当前用户的记录
    ↓
用户登出 → clearData() → 清空所有状态
```

## 技术特点

1. **多用户数据隔离** - 通过 userId 确保数据安全
2. **响应式更新** - 使用 Flow 和 StateFlow 自动刷新 UI
3. **类型安全** - 统一使用 Long 类型处理 ID
4. **错误处理** - 删除操作有确认对话框
5. **用户友好** - 空状态提示、成功消息
6. **Material Design 3** - 现代化的 UI 设计
7. **深色主题** - 统一的视觉风格

## 使用示例

### 保存思维记录

1. 打开 CBT 页面
2. 切换到"思维记录" Tab
3. 填写 6 个步骤
4. 点击"保存思维记录"
5. 看到成功提示

### 查看历史记录

1. 在 CBT 页面点击右上角"查看历史"
2. 浏览所有记录的卡片列表
3. 点击任意卡片查看详情
4. 点击删除按钮删除记录（需确认）
5. 点击"返回列表"返回

## 未来优化建议

- [ ] 添加搜索功能
- [ ] 按日期/类型筛选
- [ ] 导出为 PDF
- [ ] 思维模式趋势分析
- [ ] 添加标签功能
- [ ] 收藏重要记录
- [ ] 编辑已保存的记录

---

**更新时间**: 2025-10-21
**版本**: v1.2.0 - 思维记录存储功能
