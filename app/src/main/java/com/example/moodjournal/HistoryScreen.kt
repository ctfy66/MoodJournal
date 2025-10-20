package com.example.moodjournal

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodjournal.viewmodel.MoodViewModel

@Composable
fun HistoryScreen(
    viewModel: MoodViewModel,
    onAddMood: () -> Unit,
    onStatsClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val allEntries by viewModel.allEntries.collectAsState()
    
    val darkBackground = Color(0xFF0A0C1E)
    val cardBackground = Color(0xFF1A1D35)
    val borderColor = Color(0xFF2D3254)
    val cyanButton = Color(0xFF00E5FF)
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

            

            // History Items List
            allEntries.forEach { entry ->
                HistoryItemWithData(
                    entry = entry,
                    viewModel = viewModel,
                    cardBackground = cardBackground,
                    borderColor = borderColor,
                    white = white,
                    lightGray = lightGray
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (allEntries.isEmpty()) {
                Text(
                    text = "还没有记录，点击下方 + 按钮添加第一条心情记录吧！",
                    fontSize = 14.sp,
                    color = lightGray,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.padding(vertical = 40.dp)
                )
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
            // Stats Button (左边改成"仪表盘")
            Text(
                text = "仪表盘",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = white,
                modifier = Modifier.clickable { onStatsClick() }
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
fun HistoryItemWithData(
    entry: com.example.moodjournal.data.MoodEntry,
    viewModel: MoodViewModel,
    cardBackground: Color,
    borderColor: Color,
    white: Color,
    lightGray: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(cardBackground, RoundedCornerShape(20.dp))
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${entry.getMoodEmoji()} ${entry.getMoodName()}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = white
                )
                Text(
                    text = viewModel.formatTimestamp(entry.timestamp),
                    fontSize = 12.sp,
                    color = lightGray
                )
            }
            
            if (entry.factors.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = entry.factors.joinToString(", "),
                    fontSize = 14.sp,
                    color = lightGray
                )
            }
            
            if (entry.note.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = entry.note,
                    fontSize = 14.sp,
                    color = lightGray,
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
fun HistoryItem(
    cardBackground: Color,
    borderColor: Color,
    textColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(cardBackground, RoundedCornerShape(20.dp))
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .padding(horizontal = 20.dp, vertical = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = "Text",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = textColor
        )
    }
}
