package com.example.moodjournal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodjournal.data.ThoughtRecord
import com.example.moodjournal.viewmodel.ThoughtRecordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThoughtRecordHistoryScreen(
    viewModel: ThoughtRecordViewModel,
    onBack: () -> Unit
) {
    val darkBackground = Color(0xFF0A0C1E)
    val cardBackground = Color(0xFF1A1D35)
    val borderColor = Color(0xFF2D3254)
    val cyanText = Color(0xFF00E5FF)
    val purpleAccent = Color(0xFF7C3AED)
    val lightGray = Color(0xFF9CA3AF)
    val white = Color.White
    val redAccent = Color(0xFFEF4444)
    
    val allRecords by viewModel.allThoughtRecords.collectAsState()
    var selectedRecord by remember { mutableStateOf<ThoughtRecord?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var recordToDelete by remember { mutableStateOf<ThoughtRecord?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("思维记录历史", color = white)
                        Text(
                            "共 ${allRecords.size} 条记录",
                            fontSize = 12.sp,
                            color = lightGray
                        )
                    }
                },
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
        if (selectedRecord != null) {
            // 详情视图
            ThoughtRecordDetailView(
                record = selectedRecord!!,
                viewModel = viewModel,
                onBack = { selectedRecord = null },
                onDelete = {
                    recordToDelete = selectedRecord
                    showDeleteDialog = true
                },
                cardBackground = cardBackground,
                borderColor = borderColor,
                cyanText = cyanText,
                purpleAccent = purpleAccent,
                lightGray = lightGray,
                white = white,
                redAccent = redAccent,
                modifier = Modifier.padding(paddingValues)
            )
        } else {
            // 列表视图
            if (allRecords.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text(
                            text = "📝",
                            fontSize = 64.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "还没有思维记录",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = white
                        )
                        Text(
                            text = "开始记录和挑战负面思维吧",
                            fontSize = 14.sp,
                            color = lightGray,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    allRecords.forEach { record ->
                        ThoughtRecordCard(
                            record = record,
                            viewModel = viewModel,
                            onClick = { selectedRecord = record },
                            onDelete = {
                                recordToDelete = record
                                showDeleteDialog = true
                            },
                            cardBackground = cardBackground,
                            borderColor = borderColor,
                            cyanText = cyanText,
                            purpleAccent = purpleAccent,
                            lightGray = lightGray,
                            white = white
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
    
    // 删除确认对话框
    if (showDeleteDialog && recordToDelete != null) {
        AlertDialog(
            onDismissRequest = { 
                showDeleteDialog = false
                recordToDelete = null
            },
            title = { Text("删除思维记录", color = white) },
            text = { Text("确定要删除这条思维记录吗？此操作无法撤销。", color = lightGray) },
            confirmButton = {
                TextButton(
                    onClick = {
                        recordToDelete?.let { viewModel.deleteThoughtRecord(it) }
                        showDeleteDialog = false
                        selectedRecord = null
                        recordToDelete = null
                    }
                ) {
                    Text("删除", color = redAccent)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        recordToDelete = null
                    }
                ) {
                    Text("取消", color = lightGray)
                }
            },
            containerColor = cardBackground
        )
    }
}

@Composable
fun ThoughtRecordCard(
    record: ThoughtRecord,
    viewModel: ThoughtRecordViewModel,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    cardBackground: Color,
    borderColor: Color,
    cyanText: Color,
    purpleAccent: Color,
    lightGray: Color,
    white: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = cardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = viewModel.formatDate(record.timestamp),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = cyanText
                )
                
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "删除",
                        tint = lightGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
            if (record.distortionType.isNotBlank()) {
                Text(
                    text = "认知扭曲: ${record.distortionType}",
                    fontSize = 12.sp,
                    color = purpleAccent,
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                )
            }
            
            Text(
                text = "情境: ${record.situation.take(50)}${if (record.situation.length > 50) "..." else ""}",
                fontSize = 13.sp,
                color = white,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            
            Text(
                text = "自动化思维: ${record.automaticThought.take(50)}${if (record.automaticThought.length > 50) "..." else ""}",
                fontSize = 13.sp,
                color = lightGray,
                lineHeight = 18.sp
            )
            
            Text(
                text = viewModel.formatTimestamp(record.timestamp),
                fontSize = 11.sp,
                color = lightGray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun ThoughtRecordDetailView(
    record: ThoughtRecord,
    viewModel: ThoughtRecordViewModel,
    onBack: () -> Unit,
    onDelete: () -> Unit,
    cardBackground: Color,
    borderColor: Color,
    cyanText: Color,
    purpleAccent: Color,
    lightGray: Color,
    white: Color,
    redAccent: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 头部
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) {
                Text("← 返回列表", color = cyanText)
            }
            
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "删除",
                    tint = redAccent
                )
            }
        }
        
        // 时间和类型
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
                    text = viewModel.formatTimestamp(record.timestamp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = cyanText
                )
                
                if (record.distortionType.isNotBlank()) {
                    Text(
                        text = "认知扭曲类型: ${record.distortionType}",
                        fontSize = 14.sp,
                        color = purpleAccent,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
        
        // 1. 情境
        DetailSection(
            title = "1. 情境",
            content = record.situation,
            cardBackground = cardBackground,
            cyanText = cyanText,
            white = white
        )
        
        // 2. 自动化思维
        DetailSection(
            title = "2. 自动化思维",
            content = record.automaticThought,
            cardBackground = cardBackground,
            cyanText = cyanText,
            white = white
        )
        
        // 3. 情绪反应
        if (record.emotion.isNotBlank()) {
            DetailSection(
                title = "3. 情绪反应",
                content = record.emotion,
                cardBackground = cardBackground,
                cyanText = cyanText,
                white = white
            )
        }
        
        // 4. 支持证据
        if (record.evidence.isNotBlank()) {
            DetailSection(
                title = "4. 支持证据",
                content = record.evidence,
                cardBackground = cardBackground,
                cyanText = cyanText,
                white = white
            )
        }
        
        // 5. 替代性思维
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = purpleAccent.copy(alpha = 0.2f)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "5. 更平衡的想法 ✨",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = cyanText,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = record.alternativeThought,
                    fontSize = 14.sp,
                    color = white,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun DetailSection(
    title: String,
    content: String,
    cardBackground: Color,
    cyanText: Color,
    white: Color
) {
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
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = cyanText,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = content,
                fontSize = 14.sp,
                color = white,
                lineHeight = 20.sp
            )
        }
    }
}
