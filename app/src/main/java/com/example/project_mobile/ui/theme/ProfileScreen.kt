package com.example.project_mobile.ui.theme


import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.project_mobile.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Timestamp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController) {
    val createClient = ChitChatAPI.create()
    var expanded by remember { mutableStateOf(false) }
    var commentDialog by remember { mutableStateOf(false) }
    var logoutDialog by remember { mutableStateOf(false) }
    var favorite by remember { mutableStateOf(false) }

    var checkedState by remember { mutableStateOf(false) }


    lateinit var sharedPreferences: SharedPreferencesManager
    val contextForToast = LocalContext.current.applicationContext
    sharedPreferences = SharedPreferencesManager(contextForToast)
    val userId = sharedPreferences.userId ?: 0

    val initialUser = ProfileClass(0, "", "", "","", Timestamp(0), Timestamp(0), 0)

    var userItem by remember { mutableStateOf(initialUser) }
    var postsItems = remember { mutableStateListOf<PostClass>() }
    var userItemsList = remember { mutableStateListOf<FriendsClass>() }
    var commentItemsList = remember { mutableStateListOf<CommentClass>() }
    var commentDialogId by remember { mutableStateOf(0) }
    val postCount = postsItems.size
    val userCount = userItemsList.size
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()
    var postId by remember { mutableStateOf(0) }
    val initialLike = LikesClass(0, 0, 0,  Timestamp(0), Timestamp(0), 0)

    var likeItem by remember { mutableStateOf(initialLike) }





    LaunchedEffect(lifecycleState) {
        when (lifecycleState) {
            Lifecycle.State.DESTROYED -> {}
            Lifecycle.State.INITIALIZED -> {}
            Lifecycle.State.CREATED -> {}
            Lifecycle.State.STARTED -> {}
            Lifecycle.State.RESUMED -> {
                createClient.getUser(userId).enqueue(object : Callback<AllUserClass> {
                    override fun onResponse(
                        call: Call<AllUserClass>,
                        response: Response<AllUserClass>
                    ) {
                        if (response.isSuccessful) {
                            userItem = ProfileClass(
                                response.body()!!.user_id,
                                response.body()!!.user_name,
                                response.body()!!.email,
                                response.body()!!.img,
                                response.body()!!.gender,
                                response.body()!!.create_at,
                                response.body()!!.update_at,
                                response.body()!!.delete_at,
                            )
                        } else {
                            Toast.makeText(
                                contextForToast,
                                "User ID Not Found",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }


                    override fun onFailure(call: Call<AllUserClass>, t: Throwable) {
                        Toast.makeText(
                            contextForToast,
                            "Error onFailure " + t.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
                createClient.getUserPosts(userId).enqueue(object : Callback<List<PostClass>> {
                    override fun onResponse(
                        call: Call<List<PostClass>>,
                        response: Response<List<PostClass>>
                    ) {
                        response.body()?.forEach {
                            postsItems.add(
                                PostClass(
                                    it.post_id,
                                    it.text,
                                    it.img,
                                    it.user_id,
                                    it.create_at,
                                    it.update_at,
                                    it.delete_at,
                                    it.user_name,
                                    it.user_img,
                                    it.comment_count,
                                    it.like_count,
                                )
                            )
                        }
                    }

                    override fun onFailure(call: Call<List<PostClass>>, t: Throwable) {
                        Toast.makeText(
                            contextForToast,
                            "Error onFailure " + t.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
                createClient.getFriends(userId).enqueue(object : Callback<List<FriendsClass>> {
                    override fun onResponse(
                        call: Call<List<FriendsClass>>,
                        response: Response<List<FriendsClass>>
                    ) {
                        response.body()?.forEach {
                            userItemsList.add(
                                FriendsClass(
                                    it.user_id,
                                    it.user_name,
                                    it.email,
                                    it.img,
                                    it.gender,
                                    it.create_at,
                                    it.update_at,
                                    it.delete_at,
                                )
                            )
                        }
                    }

                    override fun onFailure(call: Call<List<FriendsClass>>, t: Throwable) {
                        Toast.makeText(
                            contextForToast,
                            "Error onFailure " + t.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })


                createClient.likePost(postId,userId).enqueue(object : Callback<LikesClass> {
                    override fun onResponse(
                        call: Call<LikesClass>,
                        response: Response<LikesClass>
                    ) {
                        if (response.isSuccessful) {
                            likeItem = LikesClass(
                                response.body()!!.status,
                                response.body()!!.user_id,
                                response.body()!!.post_id,
                                response.body()!!.create_at,
                                response.body()!!.update_at,
                                response.body()!!.delete_at,
                            )
                        } else {
                            Toast.makeText(
                                contextForToast,
                                "",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    override fun onFailure(call: Call<LikesClass>, t: Throwable) {
                        Toast.makeText(
                            contextForToast,
                            "Error onFailure " + t.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })

            }
        }
    }


    val initialPost = PostClass(0, "", "", 0, Timestamp(0), Timestamp(0),0,"","",0,0)
    var postItem by remember { mutableStateOf(initialPost) }

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
                    text = {
                        Text(
                            fontSize = 17.sp,
                            text = "คุณต้องการออกจากระบบใช่หรือไม่"
                        )
                    },
                    confirmButton = {
                        TextButton(
//                            colors = ButtonDefaults.buttonColors(Color(130, 0, 131, 255)),
                            onClick = {
                                logoutDialog = false
                                if (checkedState) {
                                    sharedPreferences.clearUserLogin()
                                    Toast.makeText(
                                        contextForToast,
                                        "Clear User Login",
                                        Toast.LENGTH_SHORT
                                    )
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
                                navController.navigate(Screen.Login.route)
                            }
                        ) {
                            Text(
                                fontSize = 17.sp,
                                color = Color.Red,
                                text = "ออกจากระบบ"
                            )
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // ภาพโปรไฟล์, จำนวนโพสต์, จำนวนเพื่อน
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(userItem.img), // เปลี่ยนเป็นรูปภาพที่ต้องการ
                            contentDescription = "Post Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(85.dp), // กำหนดความสูงของรูปภาพ
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "${userItem.user_name}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var itemClick = ProfileClass(0, "", "", "","", Timestamp(0), Timestamp(0), 0)
                    Spacer(modifier = Modifier.height(30.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = postCount.toString(),
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
                                text = (userCount-1).toString(),
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
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            itemClick = userItem
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "data",
                                ProfileClass(
                                    userItem.user_id,
                                    userItem.user_name,
                                    userItem.email,
                                    userItem.img,
                                    userItem.gender,
                                    userItem.create_at,
                                    userItem.update_at,
                                    userItem.delete_at
                                )
                            )
                            navController.navigate(Screen.EditProfile.route)
                        },
                        colors = ButtonDefaults.buttonColors(Color(130, 0, 131, 255)),
                    ) {
                        Text(text = "แก้ไขโปรไฟล์")
                    }
                }
            }
        }




        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
//            verticalAlignment = Alignment.Top
        ) {

            items(postsItems) { post ->
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
                    var itemClick = PostClass(0, "", "", 0, Timestamp(0),Timestamp(0),0,"","",0,0)
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
                                    painter = rememberAsyncImagePainter(post.user_img), // เปลี่ยนเป็นรูปภาพที่ต้องการ
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
                                text = post.user_name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            //ปุ่มมแก้ไข
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color.Transparent)
                                    .clickable {
                                        itemClick = post
                                        navController.currentBackStackEntry?.savedStateHandle?.set(
                                            "post",
                                            PostClass(
                                                post.post_id,
                                                post.text,
                                                post.img,
                                                post.user_id,
                                                post.create_at,
                                                post.update_at,
                                                post.delete_at,
                                                post.user_name,
                                                post.user_img,
                                                post.comment_count,
                                                post.like_count,
                                            )
                                        )
                                        navController.navigate(Screen.EditPost.route)
                                    }

                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit Icon", tint = Color.Black)
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
                                painter = rememberAsyncImagePainter(post.img), // เปลี่ยนเป็นรูปภาพที่ต้องการ
                                contentDescription = post.img,
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
                                text = post.text,
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
                                    postId = post.post_id
                                    createClient.likePost(postId,userId).enqueue(object : Callback<LikesClass> {
                                        override fun onResponse(
                                            call: Call<LikesClass>,
                                            response: Response<LikesClass>
                                        ) {
                                            if (response.isSuccessful) {


                                            } else {
                                                Toast.makeText(
                                                    contextForToast,
                                                    "CAN NOT LIKED",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        }
                                        override fun onFailure(call: Call<LikesClass>, t: Throwable) {
                                            Toast.makeText(
                                                contextForToast,
                                                "Error onFailure " + t.message,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    })

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
                                text = post.like_count.toString(), // จำนวนการถูกใจ
                                fontSize = 17.sp,
                                color = Color.Black
                            )

                            Spacer(modifier = Modifier.width(10.dp))
                            IconButton(
                                onClick = {
                                    commentDialog = true
                                    commentDialogId = post.post_id
                                    commentItemsList.clear()

                                    createClient.getComment(commentDialogId).enqueue(object : Callback<List<CommentClass>> {
                                        override fun onResponse(
                                            call: Call<List<CommentClass>>,
                                            response: Response<List<CommentClass>>
                                        ) {

                                            response.body()?.forEach {
                                                commentItemsList.add(
                                                    CommentClass(
                                                        it.comment_id,
                                                        it.text,
                                                        it.post_id,
                                                        it.created_at,
                                                        it.deleted_at,
                                                        it.userName,
                                                        it.img,
                                                    )
                                                )
                                            }
                                        }
                                        override fun onFailure(call: Call<List<CommentClass>>, t: Throwable) {
                                            Toast.makeText(
                                                contextForToast,
                                                "Error onFailure " + t.message,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    })
                                }
                            ) {
                                Icon(
                                    Icons.Filled.MailOutline,
                                    contentDescription = "Comment",
                                    tint = Color.Gray
                                )
                            }
                            Text(
                                text = post.comment_count.toString(), // จำนวน comment
                                fontSize = 17.sp,
                                color = Color.Black
                            )
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
                                            items(commentItemsList) { comment ->
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
                                                                .size(50.dp)
                                                                .clip(CircleShape)
                                                                .background(Color.Gray),
                                                            contentAlignment = Alignment.Center
                                                        ) {
                                                            // รูปภาพของผู้ใช้
                                                            Image(
                                                                painter = rememberAsyncImagePainter(comment.img), // เปลี่ยนเป็นรูปภาพที่ต้องการ
                                                                contentDescription = "Post Image",
                                                                modifier = Modifier
                                                                    .fillMaxWidth()
                                                                    .height(38.dp), // กำหนดความสูงของรูปภาพ
                                                                contentScale = ContentScale.Crop
                                                            )
                                                        }


                                                        Spacer(modifier = Modifier.width(16.dp))


                                                        // Text content
                                                        Column {
                                                            Text(
                                                                text = "${comment.userName}",
                                                                fontSize = 18.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = Color.Black
                                                            )
                                                            Text(
                                                                text = "${comment.text}",
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

                                                    // Add code to insert data into the database
                                                    createClient.insertComment(
                                                        newComment,
                                                        userId,
                                                        commentDialogId
                                                    ).enqueue(object : Callback<CommentClass>{
                                                        override fun onResponse(call: Call<CommentClass>, response: Response<CommentClass>) {
                                                            if(response.isSuccessful){
                                                                Toast.makeText(contextForToast,"Successfully Inserted",
                                                                    Toast.LENGTH_SHORT).show()
                                                            }
                                                        }

                                                        override fun onFailure(call: Call<CommentClass>, t: Throwable) {
                                                            Toast.makeText(contextForToast,"Error onFailure "+t.message,Toast.LENGTH_LONG).show()
                                                        }
                                                    })
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


