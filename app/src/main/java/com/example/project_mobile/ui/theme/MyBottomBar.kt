package com.example.project_mobile.ui.theme

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
@Composable
fun MyBottomBar(navController: NavHostController, contextForToast: Context) {
    val navigationItems = listOf(Screen2.Home, Screen2.Friend, Screen2.AddPost, Screen2.Profile)
    var selectedScreen by remember { mutableStateOf(0) }
    NavigationBar {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp), // Adjust the height as needed
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navigationItems.forEachIndexed { index, screen ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = null,
                            modifier = Modifier.size(35.dp)
                        )
                    },
                    selected = (selectedScreen == index),
                    onClick = {
                        if (navController.currentBackStack.value.size >= 2) {
                            navController.popBackStack()
                        }
                        selectedScreen = index
                        navController.navigate(screen.route)
                        Toast.makeText(contextForToast, screen.name, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}

