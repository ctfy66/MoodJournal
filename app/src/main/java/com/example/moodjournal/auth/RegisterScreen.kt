package com.example.moodjournal.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodjournal.viewmodel.AuthState

@Composable
fun RegisterScreen(
    authState: AuthState,
    onRegister: (String, String, String, String?) -> Unit,
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    
    val darkBackground = Color(0xFF0A0C1E)
    val cardBackground = Color(0xFF1A1D35)
    val borderColor = Color(0xFF2D3254)
    val cyanButton = Color(0xFF00E5FF)
    val white = Color.White
    val lightGray = Color(0xFF9CA3AF)
    val errorRed = Color(0xFFEF4444)
    val successGreen = Color(0xFF10B981)
    
    // 密码强度指示
    val passwordStrength = remember(password) {
        when {
            password.length < 6 -> 0
            password.length < 8 -> 1
            password.any { it.isDigit() } && password.any { it.isLetter() } -> 2
            else -> 1
        }
    }
    
    // 监听注册成功状态
    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onRegisterSuccess()
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(darkBackground, Color(0xFF1A1D35))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // Back Button
            IconButton(
                onClick = onNavigateToLogin,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "返回",
                    tint = white
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Logo
                Text(
                    text = "💙",
                    fontSize = 64.sp
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Title
                Text(
                    text = "创建账号",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = white
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "开始记录你的心情之旅",
                    fontSize = 16.sp,
                    color = lightGray
                )
                
                Spacer(modifier = Modifier.height(40.dp))
                
                // Username Field
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("用户名", color = lightGray) },
                    supportingText = { Text("3-20字符，仅限字母数字下划线和中文", color = lightGray, fontSize = 12.sp) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(cardBackground, RoundedCornerShape(12.dp)),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = white,
                        unfocusedTextColor = white,
                        focusedBorderColor = cyanButton,
                        unfocusedBorderColor = borderColor,
                        cursorColor = cyanButton
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Email Field (Optional)
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("邮箱（可选）", color = lightGray) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(cardBackground, RoundedCornerShape(12.dp)),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = white,
                        unfocusedTextColor = white,
                        focusedBorderColor = cyanButton,
                        unfocusedBorderColor = borderColor,
                        cursorColor = cyanButton
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Password Field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("密码", color = lightGray) },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Text(
                                text = if (passwordVisible) "隐藏" else "显示",
                                color = lightGray,
                                fontSize = 12.sp
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(cardBackground, RoundedCornerShape(12.dp)),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = white,
                        unfocusedTextColor = white,
                        focusedBorderColor = cyanButton,
                        unfocusedBorderColor = borderColor,
                        cursorColor = cyanButton
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
                )
                
                // Password Strength Indicator
                if (password.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        repeat(3) { index ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(4.dp)
                                    .background(
                                        color = when {
                                            index <= passwordStrength -> when (passwordStrength) {
                                                0 -> errorRed
                                                1 -> Color(0xFFF59E0B) // Orange
                                                else -> successGreen
                                            }
                                            else -> borderColor
                                        },
                                        shape = RoundedCornerShape(2.dp)
                                    )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = when (passwordStrength) {
                            0 -> "密码强度：弱"
                            1 -> "密码强度：中等"
                            else -> "密码强度：强"
                        },
                        fontSize = 12.sp,
                        color = when (passwordStrength) {
                            0 -> errorRed
                            1 -> Color(0xFFF59E0B)
                            else -> successGreen
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Confirm Password Field
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("确认密码", color = lightGray) },
                    singleLine = true,
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Text(
                                text = if (confirmPasswordVisible) "隐藏" else "显示",
                                color = lightGray,
                                fontSize = 12.sp
                            )
                        }
                    },
                    isError = confirmPassword.isNotEmpty() && password != confirmPassword,
                    supportingText = {
                        if (confirmPassword.isNotEmpty() && password != confirmPassword) {
                            Text("两次密码不一致", color = errorRed, fontSize = 12.sp)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(cardBackground, RoundedCornerShape(12.dp)),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = white,
                        unfocusedTextColor = white,
                        focusedBorderColor = cyanButton,
                        unfocusedBorderColor = borderColor,
                        errorBorderColor = errorRed,
                        cursorColor = cyanButton
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            if (username.isNotBlank() && password.isNotBlank() && password == confirmPassword) {
                                onRegister(username, password, confirmPassword, email.ifBlank { null })
                            }
                        }
                    )
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Error Message
                if (authState is AuthState.Error) {
                    Text(
                        text = authState.message,
                        color = errorRed,
                        fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                // Register Button
                Button(
                    onClick = {
                        if (username.isNotBlank() && password.isNotBlank() && password == confirmPassword) {
                            onRegister(username, password, confirmPassword, email.ifBlank { null })
                        }
                    },
                    enabled = authState !is AuthState.Loading &&
                            username.isNotBlank() &&
                            password.isNotBlank() &&
                            password == confirmPassword,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = cyanButton,
                        contentColor = darkBackground,
                        disabledContainerColor = borderColor,
                        disabledContentColor = lightGray
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (authState is AuthState.Loading) {
                        CircularProgressIndicator(
                            color = darkBackground,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "注册",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Login Link
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "已有账号？",
                        color = lightGray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "立即登录",
                        color = cyanButton,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onNavigateToLogin() }
                    )
                }
            }
        }
    }
}
