package com.example.project_mobile.ui.theme
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen2.Home.route

    ) {
        composable(route = Screen.Login.route) { navBackStackEntry ->
            LoginScreen(navController)
        }
        composable(route = Screen.Register.route) { navBackStackEntry ->
            RegisterScreen(navController)
        }
//        composable(route = Screen.AllUser.route) { navBackStackEntry ->
//            AllUserScreen(navController)
//        }
//        composable(route = Screen2.Profile.route) { navBackStackEntry ->
//            ProfileScreen(navController)
//        }


//        fang
        composable(
            route = Screen2.Home.route
        ) {
            HomeScreen(navController)
        }
        composable(
            route = Screen2.Friend.route
        ) {
            FriendScreen(navController)
        }

//        composable(
//            route = Screen.Add.route) {
//            AddScreen()
//        }
        composable(
            route = Screen2.Notification.route) {
            NotificationScreen(navController)
        }
        composable(
            route = Search.SearchFriend.route) {
            SearchFriend(navController)
        }
        composable(
            route = Search.SearchAccout.route) {
            SearchAccout(navController)
        }


//        pong
        composable(route = Screen.Profile.route) { navBackStackEntry ->
            ProfileScreen(navController)
        }
        composable(route = Screen.EditProfile.route) { navBackStackEntry ->
            EditProfileScreen(navController)
        }
        composable(route = Screen.ImageProfile.route) { navBackStackEntry ->
            ImageProfile(navController)
        }





    }
}