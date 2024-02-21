package com.example.project_mobile.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@Composable
fun FriendScreen(navController: NavHostController) {
    val contextForToast = LocalContext.current
    var deleteDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically // Align content to the center vertically
        ) {
            Text(
                text = "เพื่อน",
                textAlign = TextAlign.Start, // Align text to the center horizontally
                style = TextStyle(
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier
                    .weight(1f) // Take up the available space
                    .padding(10.dp)
            )
            // ... (other elements)


            Button(
                onClick = {
                    // Perform search
                    navController.navigate(Search.SearchFriend.route)
                },
                colors = ButtonDefaults.buttonColors(Color(130, 0, 131, 255)),
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            }
        }




        // Divider line between "Chit Chat" and LazyColumn
        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
        )


        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(5) { index ->
                // Create layout for each post
                Card(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .fillMaxWidth()
                        .height(110.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    ),
                    shape = RoundedCornerShape(corner = CornerSize(16.dp)),
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Circular image placeholder
                        Box(
                            modifier = Modifier
                                .size(80.dp) // Adjust size as needed
                                .clip(CircleShape)
                                .background(Color.Gray) // Placeholder color
                        )


                        Spacer(modifier = Modifier.width(16.dp))


                        // Text content
                        Column {
                            Text(
                                text = "เพื่อน ${index + 1}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                text = "ชื่อ ${index + 1}",
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        }
                        Spacer(modifier = Modifier.width(100.dp))
                        Button(
                            onClick = {
                                // Perform delete action
                                deleteDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(Color(248, 222, 248, 255)),
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Text(
                                text = "ลบออก",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(130, 0, 131, 255)
                                )
                            )
                        }
                        if (deleteDialog) {
                            AlertDialog(
                                containerColor = Color.White,
                                onDismissRequest = { deleteDialog = false },
                                title = { Text(text = "แจ้งเตือน") },
                                text = { Text(
                                    fontSize = 17.sp,
                                    text = "คุณต้องการลบเพื่อนใช่หรือไม่") },
                                confirmButton = {
                                    TextButton(
//                            colors = ButtonDefaults.buttonColors(Color(130, 0, 131, 255)),
                                        onClick = {
                                            deleteDialog = false
                                        }
                                    ) {
                                        Text(
                                            fontSize = 17.sp,
                                            color = Color.Red,
                                            text = "ลบ")
                                    }
                                },
                                dismissButton = {
                                    TextButton(
                                        onClick = {
                                            deleteDialog = false
                                        }
                                    ) {
                                        Text(
                                            fontSize = 17.sp,
//                                color = Color.Blue,
                                            text = "ยกเลิก",
                                        )
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MyBottomBar(navController = navController, contextForToast = contextForToast)
    }
}
