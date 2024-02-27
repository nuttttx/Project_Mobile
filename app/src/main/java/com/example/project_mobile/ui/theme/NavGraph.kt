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
//        startDestination = Screen.EditPost.route

    ) {
        composable(route = Screen.Login.route) { navBackStackEntry ->
            LoginScreen(navController)
        }
        composable(route = Screen.Register.route) { navBackStackEntry ->
            RegisterScreen(navController)
        }
        composable(route = Screen.AllUser.route) { navBackStackEntry ->
            AllUserScreen(navController)
        }


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
        composable(
            route = Screen.RequestsFriend.route
        ) {
            RequestsFriend(navController)
        }


        composable(
            route = Search.SearchFriend.route) {
            SearchFriend(navController)
        }
        composable(
            route = Search.SearchAccount.route) {
            SearchAccount(navController)
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

        //P nut
        composable(route = Screen2.AddPost.route) {
           AddPost(navController)
        }
        composable(route = Screen.EditPost.route) { navBackStackEntry ->
            EditPost(navController)
        }







    }
}