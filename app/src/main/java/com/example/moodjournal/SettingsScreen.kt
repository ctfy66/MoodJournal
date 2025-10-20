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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodjournal.viewmodel.MoodViewModel

@Composable
fun SettingsScreen(
    viewModel: MoodViewModel,
    onBackToDashboard: () -> Unit,
    onLogout: () -> Unit = {}
) {
    val darkBackground = Color(0xFF0A0C1E)
    val cardBackground = Color(0xFF1A1D35)
    val borderColor = Color(0xFF2D3254)
    val cyanAccent = Color(0xFF00E5FF)
    val white = Color.White
    val lightGray = Color(0xFF9CA3AF)
    val redWarning = Color(0xFFEF4444)

    val isLoggedIn = true // 总是已登录，因为必须登录才能访问此页面
    var username by remember { mutableStateOf("用户") }
    var userEmail by remember { mutableStateOf("") }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(true) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

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
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 32.dp),
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
                    text = "设置",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = white
                )
                
                Spacer(modifier = Modifier.size(24.dp))
            }

            // User Profile Section
            if (isLoggedIn) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(cardBackground, RoundedCornerShape(16.dp))
                        .border(1.dp, borderColor, RoundedCornerShape(16.dp))
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(cyanAccent),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = username.take(1).uppercase(),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = darkBackground
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = username,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = white
                    )
                    
                    if (userEmail.isNotEmpty()) {
                        Text(
                            text = userEmail,
                            fontSize = 14.sp,
                            color = lightGray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = { /* Edit profile */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("编辑个人资料", color = white)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Settings Sections
            SettingsSection(
                title = "应用设置",
                cardBackground = cardBackground,
                borderColor = borderColor
            ) {
                SettingItem(
                    icon = Icons.Default.Notifications,
                    title = "通知提醒",
                    subtitle = "每天提醒记录心情",
                    white = white,
                    lightGray = lightGray,
                    trailing = {
                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { notificationsEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = white,
                                checkedTrackColor = cyanAccent,
                                uncheckedThumbColor = lightGray,
                                uncheckedTrackColor = borderColor
                            )
                        )
                    }
                )
                
                Divider(color = borderColor, thickness = 1.dp)
                
                SettingItem(
                    icon = Icons.Default.Star,
                    title = "深色模式",
                    subtitle = "已启用",
                    white = white,
                    lightGray = lightGray,
                    trailing = {
                        Switch(
                            checked = darkModeEnabled,
                            onCheckedChange = { darkModeEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = white,
                                checkedTrackColor = cyanAccent,
                                uncheckedThumbColor = lightGray,
                                uncheckedTrackColor = borderColor
                            )
                        )
                    }
                )
                
                Divider(color = borderColor, thickness = 1.dp)
                
                SettingItem(
                    icon = Icons.Default.Settings,
                    title = "语言",
                    subtitle = "简体中文",
                    white = white,
                    lightGray = lightGray,
                    trailing = {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = lightGray
                        )
                    },
                    onClick = { /* Change language */ }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Data & Privacy
            SettingsSection(
                title = "数据与隐私",
                cardBackground = cardBackground,
                borderColor = borderColor
            ) {
                SettingItem(
                    icon = Icons.Default.Build,
                    title = "数据备份",
                    subtitle = "备份到云端",
                    white = white,
                    lightGray = lightGray,
                    trailing = {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = lightGray
                        )
                    },
                    onClick = { /* Backup data */ }
                )
                
                Divider(color = borderColor, thickness = 1.dp)
                
                SettingItem(
                    icon = Icons.Default.Lock,
                    title = "隐私设置",
                    subtitle = "数据加密与权限",
                    white = white,
                    lightGray = lightGray,
                    trailing = {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = lightGray
                        )
                    },
                    onClick = { /* Privacy settings */ }
                )
                
                Divider(color = borderColor, thickness = 1.dp)
                
                SettingItem(
                    icon = Icons.Default.Delete,
                    title = "清除数据",
                    subtitle = "删除所有心情记录",
                    white = white,
                    lightGray = lightGray,
                    trailing = {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = lightGray
                        )
                    },
                    onClick = { showDeleteDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // About
            SettingsSection(
                title = "关于",
                cardBackground = cardBackground,
                borderColor = borderColor
            ) {
                SettingItem(
                    icon = Icons.Default.Info,
                    title = "应用版本",
                    subtitle = "1.0.0",
                    white = white,
                    lightGray = lightGray
                )
                
                Divider(color = borderColor, thickness = 1.dp)
                
                SettingItem(
                    icon = Icons.Default.Info,
                    title = "帮助与反馈",
                    subtitle = "常见问题和联系我们",
                    white = white,
                    lightGray = lightGray,
                    trailing = {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = lightGray
                        )
                    },
                    onClick = { /* Help */ }
                )
                
                Divider(color = borderColor, thickness = 1.dp)
                
                SettingItem(
                    icon = Icons.Default.Info,
                    title = "隐私政策",
                    subtitle = null,
                    white = white,
                    lightGray = lightGray,
                    trailing = {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = lightGray
                        )
                    },
                    onClick = { /* Privacy policy */ }
                )
            }

            // Logout Button
            if (isLoggedIn) {
                Spacer(modifier = Modifier.height(32.dp))
                
                Button(
                    onClick = { showLogoutDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, redWarning),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "登出",
                        tint = redWarning,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "退出登录",
                        color = redWarning,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        // Logout Confirmation Dialog
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                containerColor = cardBackground,
                title = {
                    Text(
                        text = "确认退出",
                        color = white,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                text = {
                    Text(
                        text = "您确定要退出登录吗？您的数据会保留，下次登录时可继续使用。",
                        color = lightGray
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showLogoutDialog = false
                            onLogout()
                        }
                    ) {
                        Text("确定", color = redWarning, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showLogoutDialog = false }
                    ) {
                        Text("取消", color = lightGray)
                    }
                }
            )
        }
        
        // Delete Data Confirmation Dialog
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                containerColor = cardBackground,
                title = {
                    Text(
                        text = "确认清除数据",
                        color = white,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                text = {
                    Text(
                        text = "您确定要删除所有心情记录吗？此操作无法撤销！",
                        color = lightGray
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteAllEntries()
                            showDeleteDialog = false
                        }
                    ) {
                        Text("确定删除", color = redWarning, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDeleteDialog = false }
                    ) {
                        Text("取消", color = lightGray)
                    }
                }
            )
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    cardBackground: Color,
    borderColor: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(cardBackground, RoundedCornerShape(16.dp))
                .border(1.dp, borderColor, RoundedCornerShape(16.dp))
        ) {
            content()
        }
    }
}

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    subtitle: String?,
    white: Color,
    lightGray: Color,
    trailing: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = white,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = white
            )
            
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = lightGray,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
        
        if (trailing != null) {
            trailing()
        }
    }
}
