package com.example.moodjournal

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodjournal.viewmodel.MoodViewModel
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun StatsScreen(
    viewModel: MoodViewModel,
    onAddMood: () -> Unit,
    onDashboardClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val entryCount by viewModel.entryCount.collectAsState()
    val mostCommonMood by viewModel.mostCommonMood.collectAsState()
    
    val darkBackground = Color(0xFF0A0C1E)
    val cardBackground = Color(0xFF1A1D35)
    val borderColor = Color(0xFF2D3254)
    val cyanButton = Color(0xFF00E5FF)
    val purpleAccent = Color(0xFF7C3AED)
    val white = Color.White
    val lightGray = Color(0xFF9CA3AF)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
                .padding(bottom = 100.dp),
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

            // Mood Distribution Chart
            StatCard(
                title = "情绪分布",
                cardBackground = cardBackground,
                borderColor = borderColor,
                white = white
            ) {
                MoodPieChart(
                    viewModel = viewModel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Top Factors Chart
            StatCard(
                title = "主要影响因素",
                cardBackground = cardBackground,
                borderColor = borderColor,
                white = white
            ) {
                TopFactorsChart(
                    viewModel = viewModel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    purpleColor = purpleAccent,
                    cyanColor = cyanButton,
                    lightGray = lightGray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Stats Summary
            StatCard(
                title = "本月统计",
                cardBackground = cardBackground,
                borderColor = borderColor,
                white = white
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    StatItem("总记录数", "$entryCount", lightGray, white)
                    Spacer(modifier = Modifier.height(12.dp))
                    StatItem("最常见情绪", mostCommonMood, lightGray, white)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
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
            // Dashboard Button
            Text(
                text = "仪表盘",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = white,
                modifier = Modifier.clickable { onDashboardClick() }
            )

            // Add Button (Center)
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(cyanButton, shape = androidx.compose.foundation.shape.CircleShape)
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
fun StatCard(
    title: String,
    cardBackground: Color,
    borderColor: Color,
    white: Color,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(cardBackground, RoundedCornerShape(16.dp))
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = white,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        content()
    }
}

@Composable
fun StatItem(label: String, value: String, labelColor: Color, valueColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = labelColor
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = valueColor
        )
    }
}

@Composable
fun MoodPieChart(viewModel: MoodViewModel, modifier: Modifier = Modifier) {
    val monthEntries by viewModel.monthEntries.collectAsState()
    val moodDistribution by viewModel.moodDistribution.collectAsState()
    
    if (monthEntries.isEmpty() || moodDistribution.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "暂无数据\n开始记录心情后这里会显示统计信息",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
        return
    }
    
    // Map emoji to color
    val emojiToColor = mapOf(
        "😄" to Color(0xFF10B981),
        "🙂" to Color(0xFF3B82F6),
        "😐" to Color(0xFF8B5CF6),
        "😔" to Color(0xFFF59E0B),
        "😠" to Color(0xFFEF4444)
    )
    
    val moods = moodDistribution.map { (emoji, percentage) ->
        Triple(emoji, percentage, emojiToColor[emoji] ?: Color.Gray)
    }.sortedByDescending { it.second }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Pie Chart
        Canvas(modifier = Modifier.size(120.dp)) {
            var startAngle = -90f
            moods.forEach { (_, percentage, color) ->
                val sweepAngle = percentage * 360f
                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    size = size
                )
                startAngle += sweepAngle
            }
        }

        // Legend
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            moods.forEach { (emoji, percentage, color) ->
                val percentageInt = (percentage * 100).toInt()
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(color, RoundedCornerShape(2.dp))
                    )
                    Text(
                        text = "$emoji $percentageInt%",
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun WeeklyTrendChart(modifier: Modifier = Modifier, cyanColor: Color) {
    val weekData = listOf(0.3f, 0.5f, 0.4f, 0.7f, 0.6f, 0.8f, 0.5f)
    val days = listOf("周一", "周二", "周三", "周四", "周五", "周六", "周日")

    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            val barWidth = size.width / (weekData.size * 2)
            val spacing = barWidth

            weekData.forEachIndexed { index, value ->
                val barHeight = size.height * value
                val x = index * (barWidth + spacing) + spacing
                val y = size.height - barHeight

                drawRoundRect(
                    color = cyanColor,
                    topLeft = Offset(x, y),
                    size = Size(barWidth, barHeight),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
                )
            }
        }

        // Days labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            days.forEach { day ->
                Text(
                    text = day,
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun TopFactorsChart(
    viewModel: MoodViewModel,
    modifier: Modifier = Modifier,
    purpleColor: Color,
    cyanColor: Color,
    lightGray: Color
) {
    val topFactors by viewModel.topFactors.collectAsState()
    
    if (topFactors.isEmpty()) {
        Box(
            modifier = modifier.padding(vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "暂无数据",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
        return
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        topFactors.forEach { (factor, percentage) ->
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = factor,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                    Text(
                        text = "${(percentage * 100).toInt()}%",
                        fontSize = 14.sp,
                        color = lightGray
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(Color(0xFF2D3254), RoundedCornerShape(4.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(percentage)
                            .fillMaxHeight()
                            .background(
                                if (percentage > 0.6f) purpleColor else cyanColor,
                                RoundedCornerShape(4.dp)
                            )
                    )
                }
            }
        }
    }
}
