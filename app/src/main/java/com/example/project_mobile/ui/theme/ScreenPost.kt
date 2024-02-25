package com.example.project_mobile.ui.theme

import androidx.compose.ui.graphics.vector.ImageVector


sealed class ScreenPost (val route: String, val name: String, val icon: ImageVector){
//    object Add : Screen2(route = "add_screen", name = "เพิ่ม", icon = Icons.Default.AddCircle)
    object Addphoto: Screen(route = "Addphoto_screen", name = "Addphoto")
    object Editpost: Screen(route = "EditPost", name = "EditPost")
}

