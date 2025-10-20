package com.example.moodjournal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.moodjournal.auth.LoginScreen
import com.example.moodjournal.auth.RegisterScreen
import com.example.moodjournal.auth.WelcomeScreen
import com.example.moodjournal.ui.theme.MoodJournalTheme
import com.example.moodjournal.viewmodel.AuthViewModel
import com.example.moodjournal.viewmodel.MoodViewModel

class MainActivity : ComponentActivity() {
    private val moodViewModel: MoodViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoodJournalTheme {
                MoodJournalApp(moodViewModel, authViewModel)
            }
        }
    }
}

@Composable
fun MoodJournalApp(moodViewModel: MoodViewModel, authViewModel: AuthViewModel) {
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val authState by authViewModel.authState.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()
    
    var currentScreen by remember { mutableStateOf("welcome") }
    
    // 当用户登录成功后，设置 MoodViewModel 的 userId
    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            println("MainActivity: Current user changed to ${user.username} (id=${user.id})")
            moodViewModel.setUserId(user.id)
            println("MainActivity: MoodViewModel userId set to ${user.id}")
        } ?: run {
            println("MainActivity: Current user is null, clearing MoodViewModel data")
            moodViewModel.clearData()
        }
    }
    
    // 调试：显示当前状态
    LaunchedEffect(Unit) {
        println("MainActivity: App started")
        println("MainActivity: isLoggedIn=$isLoggedIn")
    }
    
    when (currentScreen) {
        "welcome" -> {
            WelcomeScreen(
                isLoggedIn = isLoggedIn,
                onNavigateToLogin = { currentScreen = "login" },
                onNavigateToDashboard = { currentScreen = "dashboard" }
            )
        }
        "login" -> {
            LoginScreen(
                authState = authState,
                onLogin = { username, password ->
                    authViewModel.login(username, password)
                },
                onNavigateToRegister = { 
                    authViewModel.resetAuthState()
                    currentScreen = "register" 
                },
                onLoginSuccess = { currentScreen = "dashboard" }
            )
        }
        "register" -> {
            RegisterScreen(
                authState = authState,
                onRegister = { username, password, confirmPassword, email ->
                    authViewModel.register(username, password, confirmPassword, email)
                },
                onNavigateToLogin = { 
                    authViewModel.resetAuthState()
                    currentScreen = "login" 
                },
                onRegisterSuccess = { 
                    // 注册成功后直接进入主页（已自动登录）
                    authViewModel.resetAuthState()
                    currentScreen = "dashboard"
                }
            )
        }
        "dashboard" -> {
            DashboardScreen(
                viewModel = moodViewModel,
                onAddMood = { currentScreen = "journal" },
                onViewAllRecords = { currentScreen = "history" },
                onStatsClick = { currentScreen = "stats" },
                onSettingsClick = { currentScreen = "settings" }
            )
        }
        "stats" -> {
            StatsScreen(
                viewModel = moodViewModel,
                onAddMood = { currentScreen = "journal" },
                onDashboardClick = { currentScreen = "dashboard" },
                onSettingsClick = { currentScreen = "settings" }
            )
        }
        "history" -> {
            HistoryScreen(
                viewModel = moodViewModel,
                onAddMood = { currentScreen = "journal" },
                onStatsClick = { currentScreen = "dashboard" },
                onSettingsClick = { currentScreen = "settings" }
            )
        }
        "settings" -> {
            SettingsScreen(
                viewModel = moodViewModel,
                onBackToDashboard = { currentScreen = "dashboard" },
                onLogout = { 
                    authViewModel.logout()
                    currentScreen = "login"
                }
            )
        }
        "journal" -> {
            MoodJournalScreen(
                viewModel = moodViewModel,
                onLogSuccess = { currentScreen = "success" },
                onBackToDashboard = { currentScreen = "dashboard" }
            )
        }
        "success" -> {
            LogSuccessScreen(
                onGoToDashboard = { currentScreen = "dashboard" }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MoodJournalPreview() {
    MoodJournalTheme {
        // Preview without ViewModel
    }
}