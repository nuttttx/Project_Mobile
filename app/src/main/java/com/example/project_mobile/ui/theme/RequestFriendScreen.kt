package com.example.project_mobile.ui.theme

import android.util.Log
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun RequestsFriend (navController: NavHostController) {
    var contextForToast = LocalContext.current.applicationContext
    var sharedPreferences = SharedPreferencesManager(contextForToast)
    var userId = sharedPreferences.userId ?: 0
    val createClient = ChitChatAPI.create()
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()
    val friendRequestsItemsList = remember { mutableStateListOf<FriendClass>() }


    LaunchedEffect(Unit) {
        createClient.getFriendRequests(userId).enqueue(object : Callback<List<FriendClass>> {
            override fun onResponse(call: Call<List<FriendClass>>, response: Response<List<FriendClass>>) {
                if (response.isSuccessful) {
                    val friends = response.body() ?: emptyList()

                    // Filter out the current user from the friends list
                    friendRequestsItemsList.clear()
                    friendRequestsItemsList.addAll(friends.filter { it.user_id != userId})

                    Log.d("FriendScreen", " $friendRequestsItemsList")
                } else {
                    Log.e("FriendScreen", "API call failed with code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<FriendClass>>, t: Throwable) {
                Log.e("FriendScreen", "API call failed with exception: ${t.message}", t)
            }
        })
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

            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = "คำขอเป็นเพื่อน",
                textAlign = TextAlign.Start, // Align text to the center horizontally
                style = TextStyle(
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier
                    .weight(1f) // Take up the available space
                    .padding(10.dp)
            )
        }

        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(
                items = friendRequestsItemsList.toList()
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
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
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
                        Column(
                            modifier = Modifier.weight(1f) // Consume remaining space
                        ) {
                                Text(
                                    text = item.user_name,
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


                            IconButton(
                                onClick = {
                                    createClient.acceptFriendRequest(item.requestId).enqueue(object : Callback<Void> {
                                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                            if (response.isSuccessful) {
                                                friendRequestsItemsList.removeAt(index)
                                                Toast.makeText(contextForToast, "ยอมรับเป็นเพื่อนสำเร็จ", Toast.LENGTH_LONG).show()
                                            } else {
                                                Toast.makeText(contextForToast, "ล้มเหลว", Toast.LENGTH_LONG).show()
                                            }
                                        }

                                        override fun onFailure(call: Call<Void>, t: Throwable) {
                                            Toast.makeText(contextForToast, "Error onFailure: " + t.message, Toast.LENGTH_LONG).show()
                                        }
                                    })

                                }
                            ) {
                                Icon(
                                    Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = Color.Green
                                )
                            }
                            Spacer(modifier = Modifier.width(2.dp))

                            IconButton(
                                onClick = {
                                    createClient.deleteFriendRequest(item.requestId).enqueue(object : Callback<Void> {
                                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                            if (response.isSuccessful) {
                                                friendRequestsItemsList.removeAt(index)
                                                Toast.makeText(contextForToast, "ลบสำเร็จ", Toast.LENGTH_LONG).show()
                                            } else {
                                                Toast.makeText(contextForToast, "ล้มเหลว", Toast.LENGTH_LONG).show()
                                            }
                                        }

                                        override fun onFailure(call: Call<Void>, t: Throwable) {
                                            Toast.makeText(contextForToast, "Error onFailure: " + t.message, Toast.LENGTH_LONG).show()
                                        }
                                    })
                                }
                            ) {
                                Icon(
                                    Icons.Filled.Clear,
                                    contentDescription = null,
                                    tint = Color.Red
                                )
                            }
                        }
                    }

            }
        }
    }
}
