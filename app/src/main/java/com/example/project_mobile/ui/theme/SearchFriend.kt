package com.example.project_mobile.ui.theme

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import coil.compose.rememberImagePainter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFriend(navController: NavHostController) {
    var softDeleteDialog by remember { mutableStateOf(false) }
    lateinit var sharedPreferences: SharedPreferencesManager
    val contextForToast = LocalContext.current.applicationContext
    var friendName by remember { mutableStateOf("") }
    val createClient = ChitChatAPI.create()
    sharedPreferences = SharedPreferencesManager(contextForToast)
    var userId = sharedPreferences.userId ?: 0
    var friendItemsList = remember { mutableStateListOf<FriendClass>() }
    var request by remember { mutableStateOf(0) }
    var username by remember { mutableStateOf("") }

    fun getFriends(){
        createClient.getFriends(userId).enqueue(object : Callback<List<FriendClass>> {
            override fun onResponse(call: Call<List<FriendClass>>, response: Response<List<FriendClass>>) {
                if (response.isSuccessful) {
                    val friends = response.body() ?: emptyList()

                    // Use the received friends directly without filtering
                    friendItemsList.clear()
                    friendItemsList.addAll(friends)

                    Log.d("FriendScreen", "เพื่อน: $friendItemsList")
                } else {
                    Log.e("FriendScreen", "API call failed with code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<FriendClass>>, t: Throwable) {
                Log.e("FriendScreen", "API call failed with exception: ${t.message}", t)
            }
        })
    }


    LaunchedEffect(Unit) {
        getFriends()
    }
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
                    navController.navigate(Screen2.Friend.route)
                },
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(start = 16.dp),// Corrected position of .shape
                value = friendName,
                onValueChange = { newFriendName ->
                    friendName = newFriendName },
                label = { Text(text = "ค้นหาเพื่อน") },
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
                    createClient.searchFriend(friendName).enqueue(object : Callback<FriendClass> {
                        override fun onResponse(call: Call<FriendClass>, response: Response<FriendClass>) {
                            if (response.isSuccessful) {
                                val user = response.body()
                                if (user != null) {
                                    // Check if the searched user is the logged-in user
                                    if (user.user_id != userId) {
                                        // Filter the friendItemsList based on the friendName
                                        val filteredFriends = friendItemsList.filter { it.user_name.contains(friendName, ignoreCase = true) }
                                        friendItemsList.clear()
                                        friendItemsList.addAll(filteredFriends)

                                        if (filteredFriends.isNotEmpty()) {
                                            Toast.makeText(contextForToast, "เจอเพื่อน", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(contextForToast, "ไม่เจอเพื่อน", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        Toast.makeText(contextForToast, " ", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(contextForToast, "ไม่เจอเพื่อน", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(contextForToast, "ผิดพลาด: ${response.code()}", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<FriendClass>, t: Throwable) {
                            Toast.makeText(contextForToast, "Error onFailure: ${t.message}", Toast.LENGTH_LONG).show()
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
                items = friendItemsList.toList()
            ) { index, item ->

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
                        Column (
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = " ${item.user_name}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                text = "เพศ : ${item.gender}",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.width(75.dp))
                        Button(
                            onClick = {
                                // Perform delete action
                                softDeleteDialog = true
                                request = item.requestId
                                username = item.user_name
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
                        if (softDeleteDialog) {
                            AlertDialog(
                                containerColor = Color.White,
                                onDismissRequest = { softDeleteDialog = false },
                                title = { Text(text = "แจ้งเตือน") },
                                text = {
                                    Text(
                                        fontSize = 17.sp,
                                        text = "คุณต้องการลบ ${username} ใช่หรือไม่"
                                    )
                                },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            softDeleteDialog = false

                                            createClient.deleteFriend(request)
                                                .enqueue(object : Callback<Void> {
                                                    override fun onResponse(
                                                        call: Call<Void>,
                                                        response: Response<Void>
                                                    ) {
                                                        if (response.isSuccessful) {
                                                            // Do nothing here, let LazyColumn handle the removal

                                                            Toast.makeText(
                                                                contextForToast,
                                                                "ลบเพื่อนสำเร็จ",
                                                                Toast.LENGTH_LONG
                                                            ).show()
                                                        } else {
                                                            Toast.makeText(
                                                                contextForToast,
                                                                "ลบเพื่อนล้มเหลว",
                                                                Toast.LENGTH_LONG
                                                            ).show()
                                                        }
                                                        if (navController.currentBackStack.value.size >= 0) {
                                                            navController.popBackStack()
                                                        }
                                                        navController.navigate(Search.SearchFriend.route)
                                                    }

                                                    override fun onFailure(
                                                        call: Call<Void>, t: Throwable
                                                    ) {
                                                        Toast.makeText(
                                                            contextForToast,
                                                            "เกิดข้อผิดพลาด " + t.message,
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    }
                                                })
                                        }
                                    ) {
                                        Text(
                                            fontSize = 17.sp,
                                            color = Color.Red,
                                            text = "ลบ"
                                        )
                                    }
                                },
                                dismissButton = {
                                    TextButton(
                                        onClick = {
                                            softDeleteDialog = false
                                        }
                                    ) {
                                        Text(
                                            fontSize = 17.sp,
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
}
