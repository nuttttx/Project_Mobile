package com.example.project_mobile.ui.theme

sealed class Search(val route: String, val name: String) {
    object SearchFriend : Search(route = "search_friend", name = "ค้นหาเพื่อน")
    object SearchAccount : Search(route = "search_accout", name = "ค้นหาบัญชี")
//    pong
    object Profile: Screen(route = "profile_screen", name="Profile")
    object EditProfile: Screen(route = "editprofile_screen", name="EditProfile")
    object ImageProfile: Screen(route = "Imageprofile_screen", name="ImageProfile")
}
