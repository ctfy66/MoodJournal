package com.example.moodjournal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodjournal.viewmodel.MoodViewModel

@Composable
fun MoodJournalScreen(
    viewModel: MoodViewModel,
    onLogSuccess: () -> Unit = {},
    onBackToDashboard: () -> Unit = {}
) {
    var selectedMood by remember { mutableStateOf<Int?>(null) }
    var selectedFactors by remember { mutableStateOf(setOf<String>()) }
    var journalText by remember { mutableStateOf("") }

    val darkBackground = Color(0xFF0A0C1E)
    val cardBackground = Color(0xFF1A1D35)
    val borderColor = Color(0xFF2D3254)
    val purpleButton = Color(0xFF7C3AED)
    val cyanText = Color(0xFF00E5FF)
    val lightGray = Color(0xFF9CA3AF)
    val moodIconGray = Color(0xFF4B5563)
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
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with back button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "返回",
                    tint = white,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onBackToDashboard() }
                )
                
                Text(
                    text = "情绪日记",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = white
                )
                
                Spacer(modifier = Modifier.size(24.dp))
            }

            // Mood Question
            Text(
                text = "感觉怎么样?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Mood Icons Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val moods = listOf("😠", "😔", "😐", "🙂", "😄")
                moods.forEachIndexed { index, emoji ->
                    MoodIcon(
                        emoji = emoji,
                        isSelected = selectedMood == index,
                        onClick = { selectedMood = index },
                        backgroundColor = moodIconGray
                    )
                }
            }

            // Factors Question
            Text(
                text = "什么影响你的情绪?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Start
            )

            // Factors Grid
            val factors = listOf(
                "工作", "运动", "家庭",
                "爱好", "财务", "睡眠",
                "饮酒", "饮食", "人际关系",
                "学习", "天气", "音乐",
                "旅行", "健康"
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 28.dp)
            ) {
                factors.chunked(3).forEach { rowFactors ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowFactors.forEach { factor ->
                            FactorChip(
                                text = factor,
                                isSelected = selectedFactors.contains(factor),
                                onClick = {
                                    selectedFactors = if (selectedFactors.contains(factor)) {
                                        selectedFactors - factor
                                    } else {
                                        selectedFactors + factor
                                    }
                                },
                                borderColor = borderColor,
                                backgroundColor = cardBackground
                            )
                        }
                        // Fill remaining space if less than 3 items
                        repeat(3 - rowFactors.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            // Journal Entry Section
            Text(
                text = "写下你的想法",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Start
            )

            // Text Input
            OutlinedTextField(
                value = journalText,
                onValueChange = { journalText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .border(1.dp, borderColor, RoundedCornerShape(12.dp)),
                placeholder = {
                    Text(
                        text = "今天过得怎么样？有什么影响了你的心情吗？或者其他想记录的...",
                        color = lightGray.copy(alpha = 0.5f),
                        fontSize = 13.sp
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = cardBackground,
                    unfocusedContainerColor = cardBackground,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = lightGray,
                    unfocusedTextColor = lightGray
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Log Mood Button
            Button(
                onClick = {
                    selectedMood?.let { mood ->
                        viewModel.insertEntry(
                            moodLevel = mood,
                            factors = selectedFactors.toList(),
                            note = journalText
                        )
                        onLogSuccess()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = purpleButton
                ),
                shape = RoundedCornerShape(26.dp),
                enabled = selectedMood != null
            ) {
                Text(
                    text = "保存心情",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // CBT Link
            Text(
                text = "或使用认知行为疗法",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = cyanText,
                modifier = Modifier
                    .clickable { /* Handle CBT click */ }
                    .padding(bottom = 24.dp)
            )
        }
    }
}

@Composable
fun MoodIcon(
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    backgroundColor: Color
) {
    Box(
        modifier = Modifier
            .size(54.dp)
            .clip(CircleShape)
            .background(
                if (isSelected) backgroundColor.copy(alpha = 0.8f) else backgroundColor
            )
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) Color.White.copy(alpha = 0.3f) else Color.Transparent,
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = emoji,
            fontSize = 28.sp
        )
    }
}

@Composable
fun RowScope.FactorChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    borderColor: Color,
    backgroundColor: Color
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(40.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isSelected) borderColor.copy(alpha = 0.5f) else backgroundColor
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            color = Color.White.copy(alpha = 0.9f),
            fontWeight = FontWeight.Normal
        )
    }
}
