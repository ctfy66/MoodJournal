package com.example.moodjournal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodjournal.viewmodel.MoodViewModel

@Composable
fun DashboardScreen(
    viewModel: MoodViewModel,
    onAddMood: () -> Unit,
    onViewAllRecords: () -> Unit = {},
    onStatsClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onCBTClick: () -> Unit = {}
) {
    val recentEntries by viewModel.recentEntries.collectAsState()
    val weekEntries by viewModel.weekEntries.collectAsState()
    val monthEntries by viewModel.monthEntries.collectAsState()
    val entryCount by viewModel.entryCount.collectAsState()
    val mostCommonMood by viewModel.mostCommonMood.collectAsState()
    
    // 调试：监控数据变化
    LaunchedEffect(recentEntries.size, entryCount) {
        println("DashboardScreen: recentEntries.size=${recentEntries.size}, entryCount=$entryCount")
        println("DashboardScreen: Current userId=${viewModel.getCurrentUserIdForDebug()}")
    }
    
    val darkBackground = Color(0xFF0A0C1E)
    val cardBackground = Color(0xFF1A1D35)
    val borderColor = Color(0xFF2D3254)
    val cyanText = Color(0xFF00E5FF)
    val purpleAccent = Color(0xFF7C3AED)
    val greenAccent = Color(0xFF10B981)
    val lightGray = Color(0xFF9CA3AF)
    val white = Color.White

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "情绪日记",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = white,
                modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
            )

            // Greeting
            Text(
                text = getGreeting(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = white,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                textAlign = TextAlign.Center
            )

            // Quick Stats Cards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Total Records Card
                QuickStatCard(
                    title = "总记录",
                    value = "$entryCount",
                    icon = "📝",
                    backgroundColor = cardBackground,
                    borderColor = borderColor,
                    modifier = Modifier.weight(1f)
                )
                
                // This Month Card
                QuickStatCard(
                    title = "本月记录",
                    value = "${monthEntries.size}",
                    icon = "📅",
                    backgroundColor = cardBackground,
                    borderColor = borderColor,
                    modifier = Modifier.weight(1f)
                )
            }

            // CBT Feature Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .clickable { onCBTClick() },
                colors = CardDefaults.cardColors(containerColor = cardBackground),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🧠",
                        fontSize = 40.sp,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "认知行为疗法",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = white
                        )
                        Text(
                            text = "挑战负面思维，培养积极心态",
                            fontSize = 13.sp,
                            color = lightGray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    
                    Text(
                        text = "→",
                        fontSize = 24.sp,
                        color = cyanText,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Recent Week Mood Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(containerColor = cardBackground),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "最近心情",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = white,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Show week mood emojis or empty state
                    if (weekEntries.isNotEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            weekEntries.takeLast(7).forEach { entry ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = entry.getMoodEmoji(),
                                        fontSize = 32.sp
                                    )
                                    Text(
                                        text = viewModel.formatTimestamp(entry.timestamp).split(" ")[0],
                                        fontSize = 10.sp,
                                        color = lightGray
                                    )
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "还没有记录\n点击下方 + 号开始记录你的心情",
                            fontSize = 14.sp,
                            color = lightGray,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            // Mood Summary Card
            if (monthEntries.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBackground),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "本月概览",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = white,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "最常见情绪",
                                    fontSize = 12.sp,
                                    color = lightGray
                                )
                                Text(
                                    text = mostCommonMood,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = white,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                            
                            Text(
                                text = "查看详细 →",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = cyanText,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .clickable { onStatsClick() }
                            )
                        }
                    }
                }
            }

            // Recent Record Section
            if (recentEntries.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 100.dp)
                        .clickable { onViewAllRecords() },
                    colors = CardDefaults.cardColors(containerColor = cardBackground),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "最近记录",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = white
                            )
                            
                            Text(
                                text = "查看全部 →",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = cyanText
                            )
                        }
                        
                        val latestEntry = recentEntries.first()
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = latestEntry.getMoodEmoji(),
                                fontSize = 36.sp,
                                modifier = Modifier.padding(end = 16.dp)
                            )
                            
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = latestEntry.getMoodName(),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = white
                                )
                                
                                if (latestEntry.factors.isNotEmpty()) {
                                    Text(
                                        text = latestEntry.factors.joinToString(", "),
                                        fontSize = 12.sp,
                                        color = purpleAccent,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                                
                                if (latestEntry.note.isNotEmpty()) {
                                    Text(
                                        text = latestEntry.note.take(60) + if (latestEntry.note.length > 60) "..." else "",
                                        fontSize = 14.sp,
                                        color = lightGray,
                                        modifier = Modifier.padding(top = 8.dp),
                                        lineHeight = 20.sp
                                    )
                                }
                                
                                Text(
                                    text = viewModel.formatTimestamp(latestEntry.timestamp),
                                    fontSize = 12.sp,
                                    color = lightGray,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // Bottom Navigation Bar
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(darkBackground)
                .padding(horizontal = 32.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // View All Records Button
            Text(
                text = "历史",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = white,
                modifier = Modifier.clickable { onViewAllRecords() }
            )

            // Add Button (Center)
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(cyanText, shape = androidx.compose.foundation.shape.CircleShape)
                    .clickable { onAddMood() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "添加心情",
                    tint = darkBackground,
                    modifier = Modifier.size(36.dp)
                )
            }

            // Settings Button
            Text(
                text = "设置",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = white,
                modifier = Modifier.clickable { onSettingsClick() }
            )
        }
    }
}

@Composable
fun QuickStatCard(
    title: String,
    value: String,
    icon: String,
    backgroundColor: Color,
    borderColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = icon,
                fontSize = 28.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = title,
                fontSize = 12.sp,
                color = Color(0xFF9CA3AF),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun getGreeting(): String {
    val calendar = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("Asia/Shanghai"))
    val hour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
    
    return when (hour) {
        in 0..5 -> "夜深了 🌙"
        in 6..11 -> "早上好 ☀️"
        in 12..13 -> "中午好 🌤️"
        in 14..17 -> "下午好 ⛅"
        in 18..22 -> "晚上好 🌆"
        else -> "夜深了 🌙"
    }
}
