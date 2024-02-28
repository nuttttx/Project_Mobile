package com.example.project_mobile.ui.theme

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Timestamp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAccount(navController: NavHostController){
    var userName by remember { mutableStateOf("") }
    lateinit var sharedPreferences: SharedPreferencesManager
    var contextForToast = LocalContext.current.applicationContext
    sharedPreferences = SharedPreferencesManager(contextForToast)
    var userId = sharedPreferences.userId ?: 0
//    var userId by remember { mutableStateOf(sharedPreferences.userId ?: 0) }
    val createClient = ChitChatAPI.create()

    var userItemsList = remember { mutableStateListOf<AllUserClass>() }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically // Align content to the center vertically
        ) {
            IconButton(
                onClick = {
                    navController.navigate(Screen2.Home.route)
                },
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
                value = userName,
                onValueChange = { newUserName ->
                    userName = newUserName
                },
                label = { Text(text = "Search",) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(130, 0, 131, 255),
                    unfocusedBorderColor = Color(130, 0, 131, 255),
                    cursorColor = Color(130, 0, 131, 255),
                ),
                shape = RoundedCornerShape(8.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Button(
                colors = ButtonDefaults.buttonColors(Color(130, 0, 131, 255)),
                modifier = Modifier
                    .padding(10.dp),
                onClick = {
                    createClient.searchUser(userName).enqueue(object : Callback<AllUserClass> {
                        override fun onResponse(
                            call: Call<AllUserClass>,
                            response: Response<AllUserClass>
                        ) {
                            if (response.isSuccessful) {
                                var user = response.body()
                                if (user != null) {
                                    // Check if the searched user is the logged-in user
                                    if (user.user_id != userId) {
                                        // Clear the list and add the found user
                                        userItemsList.clear()
                                        userItemsList.add(user)
                                        Toast.makeText(contextForToast, "เจอผู้ใช้", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(contextForToast, " ", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(contextForToast, "ไม่เจอผู้ใช้", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(contextForToast, "ส่งคำขอแล้ว: ${response.code()}", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<AllUserClass>, t: Throwable) {
                            Toast.makeText(contextForToast, "Error onFailure: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                })
            {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            }
        }
        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(
                items = userItemsList.toList()
            ) { index, item ->
                // Create layout for each user
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
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(Color.Gray)
                        ){
                            Image(
                                painter = rememberImagePainter(data = item.img),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Text content
                        Column  {
                            Text(
                                text = "${item.user_name}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                text = "Gender: ${item.gender}",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.width(50.dp))
                        Button(
                            onClick = {

                                if (userItemsList.isNotEmpty()) {
                                    // ตรวจสอบว่ามีผู้ใช้ที่ค้นหาเจอแล้ว
                                    val friendToRequest = userItemsList.first() // เราเลือกเพิ่มเพื่อนแค่คนแรกในรายการ

                                    // สร้าง request โดยให้ sendUserId เป็น userId ของผู้ที่ login อยู่
                                    // และ receiveUserId เป็น userId ของเพื่อนที่ต้องการจะส่งคำขอ
                                    val request = FriendRequestData(sendUserId = userId, receiveUserId = friendToRequest.user_id )

                                    createClient.sendFriendRequest(request).enqueue(object : Callback<FriendRequestClass> {
                                        override fun onResponse(
                                            call: Call<FriendRequestClass>,
                                            response: Response<FriendRequestClass>
                                        ) {
                                            if (response.isSuccessful) {
                                                // ทำงานเมื่อคำขอถูกต้อง
                                                val friendRequest = response.body()
                                                if (friendRequest != null) {
                                                    Toast.makeText(contextForToast, "ส่งคำขอสำเร็จ", Toast.LENGTH_LONG).show()
                                                }
                                            } else {
                                                Toast.makeText(contextForToast, "ผิดพลาด: ${response.code()}", Toast.LENGTH_LONG).show()
                                            }
                                        }

                                        override fun onFailure(call: Call<FriendRequestClass>, t: Throwable) {
                                            Toast.makeText(contextForToast, "Error onFailure: ${t.message}", Toast.LENGTH_LONG).show()
                                        }
                                    })
                                } else {
                                    Toast.makeText(contextForToast, "กรุณาค้นหาผู้ใช้ก่อน", Toast.LENGTH_LONG).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(Color(248, 222, 248, 255)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp, 0.dp, 4.dp, 0.dp) // ปรับ padding ด้านหน้าและด้านหลังเพื่อไม่ให้ขยับตามข้อความ
                                .width(106.dp)
                        ) {
                            Text(
                                text = "Add Friend",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(130, 0, 131, 255)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}