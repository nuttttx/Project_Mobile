package com.example.project_mobile.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person


sealed class Screen (val route: String, val name: String  ){
    object Login : Screen(route = "login_screen", name="Login")
    object Register: Screen(route = "register_screen", name="Register")
    object AllUser: Screen(route = "alluser_screen", name="AllUser")
//    object Profile: Screen(route = "profile_screen", name="Profile")
    object Profile: Screen(route = "profile_screen", name="Profile")
    object EditProfile: Screen(route = "editprofile_screen", name="EditProfile")
    object ImageProfile: Screen(route = "Imageprofile_screen", name="ImageProfile")
    object EditPost: Screen(route = "EditPost", name = "EditPost")

    //Fang
    object RequestsFriend : Search(route = "requests_friend", name = "คำขอเป็นเพื่อน")



}
