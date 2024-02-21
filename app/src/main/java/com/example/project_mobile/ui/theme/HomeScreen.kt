package com.example.project_mobile.ui.theme
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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

import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MailOutline
//import androidx.compose.material.icons.filled.InsertComment

import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.project_mobile.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val contextForToast = LocalContext.current
    var commentDialog by remember { mutableStateOf(false) }
    var favorite by remember { mutableStateOf(false) }


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
                text = "Chit Chat",
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
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
//            verticalAlignment = Alignment.Top
        ) {
            items(5) { index ->
                // สร้างเลย์เอาต์สำหรับแต่ละโพสต์
                Card(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .fillMaxWidth()
                        .height(450.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    ),
                    shape = RoundedCornerShape(corner = CornerSize(16.dp)),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        // ใส่รูปภาพตามความเหมาะสม
                        Box(
                            modifier = Modifier
                                .height(300.dp)
                                .fillMaxWidth()
                                .clip(shape = RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.my_friends), // เปลี่ยนเป็นรูปภาพที่ต้องการ
                                contentDescription = "Post Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp), // กำหนดความสูงของรูปภาพ
                                contentScale = ContentScale.Fit
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,




                            ) {
                            Text(
                                text = "ชอบผู้ชาย InNoverbBoy ง่ะเตง", // จำนวนการถูกใจ
                                fontSize = 17.sp,
                                color = Color.Black
                            )
                        }
                        // ส่วน Icon หัวใจและ Icon Comment
                        Spacer(modifier = Modifier.height(5.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    favorite = !favorite
                                }
                            ) {
                                Icon(
                                    if (favorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                    contentDescription = "Like",
                                    tint = if (favorite) Color.Red else Color.Gray
                                )
                            }


                            Spacer(modifier = Modifier.width(1.dp))


                            Text(
                                text = " ", // จำนวนการถูกใจ
                                fontSize = 17.sp,
                                color = Color.Black
                            )




                            Spacer(modifier = Modifier.width(10.dp))
                            IconButton(
                                onClick = { commentDialog = true }
                            ) {
                                Icon(
                                    Icons.Filled.MailOutline,
                                    contentDescription = "Comment",
                                    tint = Color.Gray
                                )
                            }
                            if (commentDialog) {
                                AlertDialog(
                                    onDismissRequest = { commentDialog = false },
                                    title = {
                                        Text("ความคิดเห็นทั้งหมด")
                                    },
                                    text = {
                                        Spacer(modifier = Modifier.height(80.dp))
                                        LazyColumn(
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            items(5) { index ->
                                                // Create layout for each post
                                                Card(
                                                    modifier = Modifier
                                                        .padding(horizontal = 8.dp, vertical = 8.dp)
                                                        .fillMaxWidth()
                                                        .height(90.dp),
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
                                                                .size(50.dp) // Adjust size as needed
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
                                                                text = "ความคิดเห็น ${index + 1}",
                                                                fontSize = 14.sp,
                                                                color = Color.Black
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    },
                                    confirmButton = {
                                        Column(
                                            modifier = Modifier
                                                .height(190.dp)
                                                .fillMaxWidth()
                                                .padding(16.dp)
                                        ) {
                                            var newComment by remember { mutableStateOf("") }
                                            OutlinedTextField(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp),
                                                value = newComment,
                                                onValueChange = { newComment = it },
                                                label = { Text("แสดงความคิดเห็น") },
                                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                                    focusedBorderColor = Color(130, 0, 131, 255),
                                                    unfocusedBorderColor = Color(130, 0, 131, 255),
                                                    cursorColor = Color(130, 0, 131, 255),
                                                ),
                                                shape = RoundedCornerShape(8.dp)
                                            )


                                            Spacer(modifier = Modifier.height(1.dp))


                                            Button(
                                                onClick = {
                                                    // นำความคิดเห็นใหม่เพิ่มเข้าไปในรายการความคิดเห็น
                                                    // ทำตามต้องการ
                                                    commentDialog = false
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    Color(130, 0, 131, 255)
                                                ),
                                                modifier = Modifier
                                                    .height(50.dp)
                                                    .align(Alignment.CenterHorizontally)
                                            ) {
                                                Text("เพิ่มความคิดเห็น")
                                            }
                                        }
                                    }
                                )
                            }


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











