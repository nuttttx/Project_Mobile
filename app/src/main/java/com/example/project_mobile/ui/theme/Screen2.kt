package com.example.project_mobile.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen2(val route: String, val name: String, val icon: ImageVector) {
    object Home : Screen2(route = "home_screen", name = "หน้าหลัก", icon = Icons.Default.Home)
    object Friend : Screen2(route = "friend_screen", name = "เพื่อน", icon = Icons.Default.AccountBox)
    object Add : Screen2(route = "add_screen", name = "เพิ่ม", icon = Icons.Default.AddCircle)
    object Notification : Screen2(route = "notification_screen", name = "แจ้งเตือน", icon = Icons.Default.Notifications)
    object Profile : Screen2(route = "profile_screen", name = "บัญชี", icon = Icons.Default.Person)


}
