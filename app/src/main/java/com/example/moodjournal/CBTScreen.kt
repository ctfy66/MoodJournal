package com.example.moodjournal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class CognitiveDistortion(
    val name: String,
    val description: String,
    val example: String
)

data class ThoughtRecord(
    val situation: String,
    val automaticThought: String,
    val emotion: String,
    val distortionType: String,
    val evidence: String,
    val alternativeThought: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CBTScreen(
    onBack: () -> Unit
) {
    val darkBackground = Color(0xFF0A0C1E)
    val cardBackground = Color(0xFF1A1D35)
    val borderColor = Color(0xFF2D3254)
    val cyanText = Color(0xFF00E5FF)
    val purpleAccent = Color(0xFF7C3AED)
    val lightGray = Color(0xFF9CA3AF)
    val white = Color.White
    
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("认知扭曲", "思维记录", "练习技巧")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("认知行为疗法 (CBT)", color = white) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回",
                            tint = white
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = cardBackground
                )
            )
        },
        containerColor = darkBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tab 选择器
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = cardBackground,
                contentColor = white
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { 
                            Text(
                                title,
                                color = if (selectedTab == index) white else lightGray
                            ) 
                        }
                    )
                }
            }
            
            // 内容区域
            when (selectedTab) {
                0 -> CognitiveDistortionsTab(
                    cardBackground = cardBackground,
                    borderColor = borderColor,
                    cyanText = cyanText,
                    lightGray = lightGray,
                    white = white
                )
                1 -> ThoughtRecordTab(
                    cardBackground = cardBackground,
                    borderColor = borderColor,
                    cyanText = cyanText,
                    purpleAccent = purpleAccent,
                    lightGray = lightGray,
                    white = white
                )
                2 -> PracticeTechniquesTab(
                    cardBackground = cardBackground,
                    borderColor = borderColor,
                    cyanText = cyanText,
                    purpleAccent = purpleAccent,
                    lightGray = lightGray,
                    white = white
                )
            }
        }
    }
}

@Composable
fun CognitiveDistortionsTab(
    cardBackground: Color,
    borderColor: Color,
    cyanText: Color,
    lightGray: Color,
    white: Color
) {
    val distortions = listOf(
        CognitiveDistortion(
            "全或无思维",
            "以非黑即白的方式看待事物，没有中间地带。",
            "例：\"如果我不能完美完成，那就是彻底失败。\""
        ),
        CognitiveDistortion(
            "过度概括",
            "根据单一事件做出广泛的结论。",
            "例：\"我这次考试没考好，我永远都学不好这门课。\""
        ),
        CognitiveDistortion(
            "心理过滤",
            "只关注负面细节，忽略积极方面。",
            "例：在一次成功的演讲后，只记得那一个小失误。"
        ),
        CognitiveDistortion(
            "否定积极",
            "坚持认为积极的经历\"不算数\"。",
            "例：\"我只是运气好而已，这不能证明我有能力。\""
        ),
        CognitiveDistortion(
            "妄下结论",
            "在没有确凿证据的情况下得出负面结论。",
            "例：\"他们一定认为我很愚蠢。\"（读心术）"
        ),
        CognitiveDistortion(
            "灾难化",
            "预期最坏的情况会发生。",
            "例：\"如果我搞砸这次面试，我的人生就完了。\""
        ),
        CognitiveDistortion(
            "情绪化推理",
            "认为自己的负面情绪必然反映事实。",
            "例：\"我感觉自己是个失败者，所以我一定是个失败者。\""
        ),
        CognitiveDistortion(
            "应该陈述",
            "对自己或他人有僵化的期望。",
            "例：\"我应该总是表现完美。\" \"人们不应该让我失望。\""
        ),
        CognitiveDistortion(
            "贴标签",
            "根据错误或缺点给自己或他人贴上负面标签。",
            "例：\"我是个彻底的失败者。\" \"他是个坏人。\""
        ),
        CognitiveDistortion(
            "个人化",
            "认为自己要为不在自己控制范围内的事情负责。",
            "例：\"团队项目失败了，都是我的错。\""
        )
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            "常见的认知扭曲",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = white,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            "认知扭曲是导致负面情绪的不准确或夸张的思维模式。识别这些模式是改变它们的第一步。",
            fontSize = 14.sp,
            color = lightGray,
            modifier = Modifier.padding(bottom = 16.dp),
            lineHeight = 20.sp
        )
        
        distortions.forEach { distortion ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                colors = CardDefaults.cardColors(containerColor = cardBackground),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        distortion.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = cyanText,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        distortion.description,
                        fontSize = 14.sp,
                        color = white,
                        modifier = Modifier.padding(bottom = 8.dp),
                        lineHeight = 20.sp
                    )
                    
                    Text(
                        distortion.example,
                        fontSize = 13.sp,
                        color = lightGray,
                        lineHeight = 18.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }
        }
    }
}

@Composable
fun ThoughtRecordTab(
    cardBackground: Color,
    borderColor: Color,
    cyanText: Color,
    purpleAccent: Color,
    lightGray: Color,
    white: Color
) {
    var situation by remember { mutableStateOf("") }
    var automaticThought by remember { mutableStateOf("") }
    var emotion by remember { mutableStateOf("") }
    var selectedDistortion by remember { mutableStateOf("") }
    var evidence by remember { mutableStateOf("") }
    var alternativeThought by remember { mutableStateOf("") }
    var showSuccess by remember { mutableStateOf(false) }
    
    val distortionTypes = listOf(
        "全或无思维", "过度概括", "心理过滤", "否定积极",
        "妄下结论", "灾难化", "情绪化推理", "应该陈述",
        "贴标签", "个人化"
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            "思维记录工具",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = white,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            "记录并挑战你的自动化负面思维，培养更平衡的思维方式。",
            fontSize = 14.sp,
            color = lightGray,
            modifier = Modifier.padding(bottom = 20.dp),
            lineHeight = 20.sp
        )
        
        // 情境
        Text("1. 情境", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = cyanText)
        Text(
            "描述触发情绪的情境",
            fontSize = 12.sp,
            color = lightGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = situation,
            onValueChange = { situation = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            placeholder = { Text("例：老板在会议上批评了我的报告", color = lightGray) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = purpleAccent,
                unfocusedBorderColor = borderColor,
                focusedTextColor = white,
                unfocusedTextColor = white,
                cursorColor = purpleAccent
            )
        )
        
        // 自动化思维
        Text("2. 自动化思维", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = cyanText)
        Text(
            "当时脑海中浮现的第一个想法",
            fontSize = 12.sp,
            color = lightGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = automaticThought,
            onValueChange = { automaticThought = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            placeholder = { Text("例：我太失败了，永远做不好工作", color = lightGray) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = purpleAccent,
                unfocusedBorderColor = borderColor,
                focusedTextColor = white,
                unfocusedTextColor = white,
                cursorColor = purpleAccent
            )
        )
        
        // 情绪
        Text("3. 情绪反应", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = cyanText)
        Text(
            "你的情绪和强度（0-10分）",
            fontSize = 12.sp,
            color = lightGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = emotion,
            onValueChange = { emotion = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            placeholder = { Text("例：焦虑 8/10，羞愧 7/10", color = lightGray) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = purpleAccent,
                unfocusedBorderColor = borderColor,
                focusedTextColor = white,
                unfocusedTextColor = white,
                cursorColor = purpleAccent
            )
        )
        
        // 认知扭曲类型
        Text("4. 认知扭曲类型", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = cyanText)
        Text(
            "选择最符合的认知扭曲",
            fontSize = 12.sp,
            color = lightGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        distortionTypes.chunked(2).forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { distortion ->
                    FilterChip(
                        selected = selectedDistortion == distortion,
                        onClick = { selectedDistortion = distortion },
                        label = { Text(distortion, fontSize = 12.sp) },
                        modifier = Modifier.weight(1f),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = purpleAccent,
                            selectedLabelColor = white,
                            containerColor = cardBackground,
                            labelColor = lightGray
                        )
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 支持证据
        Text("5. 支持这个想法的证据", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = cyanText)
        Text(
            "客观事实，而非感觉",
            fontSize = 12.sp,
            color = lightGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = evidence,
            onValueChange = { evidence = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(bottom = 16.dp),
            placeholder = { Text("例：报告中确实有两处数据错误", color = lightGray) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = purpleAccent,
                unfocusedBorderColor = borderColor,
                focusedTextColor = white,
                unfocusedTextColor = white,
                cursorColor = purpleAccent
            )
        )
        
        // 替代性思维
        Text("6. 更平衡的想法", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = cyanText)
        Text(
            "基于证据的更客观的看法",
            fontSize = 12.sp,
            color = lightGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = alternativeThought,
            onValueChange = { alternativeThought = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(bottom = 20.dp),
            placeholder = { Text("例：我犯了错误，但这不意味着我是失败者。我大部分工作做得很好，下次我会更仔细检查数据", color = lightGray) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = purpleAccent,
                unfocusedBorderColor = borderColor,
                focusedTextColor = white,
                unfocusedTextColor = white,
                cursorColor = purpleAccent
            )
        )
        
        // 保存按钮
        Button(
            onClick = {
                if (situation.isNotBlank() && automaticThought.isNotBlank() && 
                    alternativeThought.isNotBlank()) {
                    showSuccess = true
                    // 清空表单
                    situation = ""
                    automaticThought = ""
                    emotion = ""
                    selectedDistortion = ""
                    evidence = ""
                    alternativeThought = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = purpleAccent),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("保存思维记录", fontSize = 16.sp, color = white)
        }
        
        if (showSuccess) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF10B981).copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "思维记录已保存！继续练习可以帮助你培养更健康的思维模式。",
                        color = white,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PracticeTechniquesTab(
    cardBackground: Color,
    borderColor: Color,
    cyanText: Color,
    purpleAccent: Color,
    lightGray: Color,
    white: Color
) {
    val techniques = listOf(
        Triple("证据检验", "问自己：支持这个想法的证据是什么？反对这个想法的证据是什么？", 
            "• 写下支持和反对的证据\n• 像法官一样客观评估\n• 寻找其他可能的解释"),
        Triple("去灾难化", "即使最坏的情况发生，我能应对吗？", 
            "• 问自己：最坏会发生什么？\n• 我能如何应对？\n• 最可能发生什么？"),
        Triple("成本收益分析", "这个想法对我有益还是有害？", 
            "• 列出保持这个想法的好处和坏处\n• 评估是否值得继续这样想\n• 考虑改变想法后的感受"),
        Triple("重新归因", "这件事还有其他原因吗？", 
            "• 列出所有可能的原因\n• 考虑环境因素\n• 避免只责怪自己或他人"),
        Triple("双重标准法", "如果朋友有这个想法，我会对他们说什么？", 
            "• 像对待朋友一样对待自己\n• 给自己同样的同情和理解\n• 用鼓励的语言替代苛刻的自我批评"),
        Triple("调查法", "我可以做个实验来测试这个想法吗？", 
            "• 设计一个小实验\n• 收集客观数据\n• 根据结果调整想法"),
        Triple("时间视角", "一年后，这件事还会重要吗？", 
            "• 想象1周、1个月、1年后\n• 把事情放在更大的背景下\n• 识别真正重要的事情")
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            "挑战负面思维的技巧",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = white,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            "使用这些技巧来质疑和重构不准确的思维模式。",
            fontSize = 14.sp,
            color = lightGray,
            modifier = Modifier.padding(bottom = 20.dp),
            lineHeight = 20.sp
        )
        
        techniques.forEach { (title, description, steps) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = cardBackground),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = cyanText,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        description,
                        fontSize = 14.sp,
                        color = white,
                        modifier = Modifier.padding(bottom = 12.dp),
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Divider(
                        color = borderColor,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    Text(
                        "如何使用：",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = purpleAccent,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    
                    Text(
                        steps,
                        fontSize = 13.sp,
                        color = lightGray,
                        lineHeight = 20.sp
                    )
                }
            }
        }
        
        // 提示卡片
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = purpleAccent.copy(alpha = 0.2f)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "💡 小贴士",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = cyanText,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    "认知行为疗法需要持续练习。开始时可能感觉不自然，但随着时间推移，你会发现识别和挑战负面思维变得更加容易。建议每天花10-15分钟进行思维记录练习。",
                    fontSize = 14.sp,
                    color = white,
                    lineHeight = 20.sp
                )
            }
        }
    }
}
