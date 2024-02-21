package com.example.project_mobile.ui.theme


import android.widget.Toast
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MailOutline

import androidx.compose.material.icons.filled.MoreVert

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.project_mobile.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun ProfileScreen(navController: NavHostController) {
    var expanded by remember { mutableStateOf(false) }
    var commentDialog by remember { mutableStateOf(false) }
    var logoutDialog by remember { mutableStateOf(false) }
    var favorite by remember { mutableStateOf(false) }
//    val contextForToast = LocalContext.current
    var checkedState by remember { mutableStateOf(false) }


    lateinit var sharedPreferences: SharedPreferencesManager
    val contextForToast = LocalContext.current.applicationContext
    sharedPreferences = SharedPreferencesManager(contextForToast)






    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 6.dp, top = 6.dp),
            horizontalArrangement = Arrangement.Start, // จัดวาง Text ให้อยู่ตรงกลาง
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "โปรไฟล์",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 6.dp),
//                style = TextStyle(color = Color(130, 0, 131, 255))


            )


            IconButton(
                onClick = {
                    logoutDialog = true
                },
            ) {
                Icon(Icons.Filled.ExitToApp, contentDescription = "Logout")
            }
            if (logoutDialog) {
                AlertDialog(
                    containerColor = Color.White,
                    onDismissRequest = { logoutDialog = false },
                    title = { Text(text = "แจ้งเตือน") },
                    text = { Text(
                        fontSize = 17.sp,
                        text = "คุณต้องการออกจากระบบใช่หรือไม่") },
                    confirmButton = {
                        TextButton(
//                            colors = ButtonDefaults.buttonColors(Color(130, 0, 131, 255)),
                            onClick = {
                                logoutDialog = false
                                if (checkedState) {
                                    sharedPreferences.clearUserLogin()
                                    Toast.makeText(contextForToast, "Clear User Login", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    sharedPreferences.clearUserAll()
                                    Toast.makeText(
                                        contextForToast,
                                        "Clear User Login and e-mail",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                if (navController.currentBackStack.value.size >= 2) {
                                    navController.popBackStack()
                                }
                                navController.navigate(Screen.Login.route)}
                        ) {
                            Text(
                                fontSize = 17.sp,
                                color = Color.Red,
                                text = "ออกจากระบบ")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                logoutDialog = false
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
//


        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
        )


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ภาพโปรไฟล์, จำนวนโพสต์, จำนวนเพื่อน
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.fang), // เปลี่ยนเป็นรูปภาพที่ต้องการ
                        contentDescription = "Post Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(85.dp), // กำหนดความสูงของรูปภาพ
                        contentScale = ContentScale.Crop
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "5",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "โพสต์",
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "3",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "เพื่อน",
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "03.fang",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Button(
                    onClick = {
                        if (navController.currentBackStack.value.size >= 2) {
                            navController.popBackStack()
                        }
                        navController.navigate(Screen.EditProfile.route)
                    },
                    colors = ButtonDefaults.buttonColors(Color(130, 0, 131, 255)),
                ) {
                    Text(text = "แก้ไขโปรไฟล์")
                }
            }
        }


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
                    shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            // รูปภาพของผู้ใช้
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray),
                                contentAlignment = Alignment.Center
                            ) {
                                // รูปภาพของผู้ใช้
                                Image(
                                    painter = painterResource(id = R.drawable.fang), // เปลี่ยนเป็นรูปภาพที่ต้องการ
                                    contentDescription = "Post Image",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(38.dp), // กำหนดความสูงของรูปภาพ
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            // ชื่อผู้ใช้
                            Text(
                                text = "User ${index + 1}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            // ปุ่มดรอปดาว (ปุ่มแก้ไขและลบ)
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color.Transparent)
                                    .clickable { expanded = true }
                            ) {
                                IconButton(onClick = {
                                    expanded = true
                                }) {
                                    Icon(
                                        Icons.Filled.MoreVert,
                                        contentDescription = "Options",
                                        tint = Color.Gray
                                    )
                                }


                                DropdownMenu(
                                    modifier = Modifier.background(Color.White),
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                ) {
                                    // Menu items
                                    DropdownMenuItem(
                                        text = { Text("แก้ไขโพสต์") },
                                        onClick = {
//                                            Toast.makeText(contextForToast, "Settings", Toast.LENGTH_SHORT).show()
                                            expanded = false
                                        },
//                                        leadingIcon = {
//                                            Icon(
//                                                Icons.Outlined.Settings,
//                                                contentDescription = null
//                                            )
//
//                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("ลบโพสต์") },
                                        onClick = {
//                                            Toast.makeText(contextForToast, "Logout", Toast.LENGTH_SHORT).show()
                                            expanded = false
                                        },
//                                        leadingIcon = {
//                                            Icon(
//                                                Icons.Outlined.Logout,
//                                                contentDescription = null
//                                            )
//                                        }
                                    )
                                }
                            }
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
                                painter = painterResource(id = R.drawable.fang), // เปลี่ยนเป็นรูปภาพที่ต้องการ
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
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(fontWeight = FontWeight.Bold)
                                    ) {
                                        append("User ${index + 1}")
                                    }
                                    append(" ชอบผู้ชาย InNoverbBoy ง่ะเตง")
                                },
                                fontSize = 17.sp,
                                color = Color.Black
                            )
                        }
                        // ส่วน Icon หัวใจและ Icon Comment
                        Spacer(modifier = Modifier.height(5.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
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


                            Spacer(modifier = Modifier.height(15.dp))


                            Text(
                                text = "10", // จำนวนการถูกใจ
                                fontSize = 17.sp,
                                color = Color.Black
                            )


//                            Spacer(modifier = Modifier.width(10.dp))
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
                                        Text("ข้อความแสดงที่นี่")
                                    },
                                    text = {
                                        LazyColumn {
                                            val comments = List(5) { "ความคิดเห็นที่ $it" }
                                            items(comments) { comment ->
                                                Card(
                                                    modifier = Modifier
                                                        .padding(horizontal = 8.dp, vertical = 8.dp)
                                                        .fillMaxWidth()
                                                        .height(70.dp),
                                                    colors = CardDefaults.cardColors(
                                                        containerColor = Color.White,
                                                    ),
                                                    elevation = CardDefaults.cardElevation(
                                                        defaultElevation = 2.dp
                                                    ),
                                                    shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                                ) {
                                                    Text(
                                                        text = comment,
                                                        modifier = Modifier.padding(16.dp)
                                                    )
                                                }


                                            }
                                        }
                                    },

                                    confirmButton = {
                                        Button(
                                            onClick = { commentDialog = false }
                                        ) {
                                            Text("ตกลง")
                                        }
                                    }
                                )
                            }




//                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "5", // จำนวนความคิดเห็น
                                fontSize = 17.sp,
                                color = Color.Black
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


