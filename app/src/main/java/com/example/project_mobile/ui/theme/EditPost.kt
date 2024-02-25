package com.example.project_mobile.ui.theme

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.project_mobile.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.sql.Timestamp


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditPost(navController: NavHostController) {
    lateinit var sharedPreferences: SharedPreferencesManager
    val contextForToast = LocalContext.current.applicationContext
    sharedPreferences = SharedPreferencesManager(contextForToast)
    val userId = sharedPreferences.userId ?: 0
    val createClient = ChitChatAPI.create()
    val initialUser = AllUserClass(
        user_id = 0,
        user_name = "",
        email = "",
        password = "",
        img = "",
        gender = "",
        create_at = Timestamp(System.currentTimeMillis()), // หรือค่าเริ่มต้นอื่น ๆ ตามที่คุณต้องการ
        update_at = Timestamp(System.currentTimeMillis()), // หรือค่าเริ่มต้นอื่น ๆ ตามที่คุณต้องการ
        delete_at = 0 // หรือค่าเริ่มต้นอื่น ๆ ตามที่คุณต้องการ
    )


    var userItem by remember { mutableStateOf(initialUser) }

    val backgroundColor = Color(130, 0, 131, 255)
    val backgroundButtonColor = Color(130, 0, 131, 255)
    var caption by remember { mutableStateOf("") }
    // State for the selected image URI
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                // Handle the selected image URI
                selectedImageUri = uri
            }
        }


    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

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
                            userItem = AllUserClass(
                                response.body()!!.user_id,
                                response.body()!!.user_name,
                                response.body()!!.email,
                                response.body()!!.password,
                                response.body()!!.img,
                                response.body()!!.gender,
                                response.body()!!.create_at,
                                response.body()!!.update_at,
                                response.body()!!.delete_at
                            )
                        } else {
                            Toast.makeText(
                                contextForToast,
                                "user ID Not Found ${userId}",
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
            }
        }
    }





    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(backgroundColor)
                .border(0.5.dp, Color.Black),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    navController.navigate(Screen2.Home.route)
                }
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Back",
                    modifier = Modifier.size(50.dp),
                    tint = Color.White


                )
            }


            Text(
                text = "Edit Post",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .weight(1f),
                color = Color.White
            )

//          ปุ่มต้องการโพสต์
            IconButton(
                onClick = {
//                    val inputStream =
//                        contextForToast.contentResolver.openInputStream(selectedImageUri!!)
//                            ?: throw Exception("Failed to open input stream")
//                    val imageFileJpg = File.createTempFile("image", ".jpg")
//                    val imageFilePng = File.createTempFile("image", ".jpg")
//                    val imageFile = File.createTempFile("image", ".jpg")
//                    val outputStream = FileOutputStream(imageFile)
//                    inputStream.copyTo(outputStream)
//                    inputStream.close()
//                    outputStream.close()
//
//
//                    val requestBodyJpg =
//                        imageFileJpg.asRequestBody("image/jpeg".toMediaTypeOrNull())
//                    val imagePartJpg = MultipartBody.Part.createFormData(
//                        name = "image",
//                        imageFileJpg.name,
//                        requestBodyJpg
//                    )
//
////                    val requestBodyPng = imageFilePng.asRequestBody("image/png".toMediaTypeOrNull())
////                    val imagePartPng = MultipartBody.Part.createFormData(
////                        name = "image",
////                        imageFilePng.name,
////                        requestBodyPng
////                    )
//
//                    val captionRequestBody = caption.toRequestBody("text/plain".toMediaTypeOrNull())
//
//
//                    createClient.editPost(
//                        captionRequestBody,
//                        imagePartJpg, // ส่งไฟล์ jpg
//                        userId
//
//                    ).enqueue(object : Callback<PostClass> {
//                        override fun onResponse(
//                            call: Call<PostClass>,
//                            response: Response<PostClass>
//                        ) {
//                            if (response.isSuccessful) {
//                                Toast.makeText(
//                                    contextForToast,
//                                    "Successfully Inserted",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            } else {
//                                Toast.makeText(
//                                    contextForToast,
//                                    "Error",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        }
//
//
//                        override fun onFailure(call: Call<PostClass>, t: Throwable) {
//                            Toast.makeText(
//                                contextForToast,
//                                "Error onFailure " + t.message,
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//
//                    })
//
//                    if (navController.currentBackStack.value.size >= 2) {
//                        navController.popBackStack()
//                    }
//                    navController.navigate(Screen2.Home.route)
                }

            ) {
                Icon(
                    Icons.Default.Done,
                    contentDescription = "Done",
                    modifier = Modifier.size(50.dp),
                    tint = Color.White
                )
            }
        }






        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Display selected image if available


            selectedImageUri?.let { uri ->
                Image(
                    painter = rememberImagePainter(uri),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .aspectRatio(1f) // ตั้งค่าอัตราส่วนเพื่อให้ภาพเป็นสี่เหลี่ยมจตุรัส
                        .fillMaxWidth() // ภาพจะเต็มความกว้างของพื้นที่ที่กำหนด
                        .border(
                            width = 1.dp,
                            color = Color.Black
                        )
                )
            }
        }
        TextField(
            value = caption,
            onValueChange = { caption = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(color = Color.White)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
                .border(1.dp, Color.Gray, RoundedCornerShape(corner = CornerSize(16.dp)))
                .shadow(5.dp),
            shape = RoundedCornerShape(corner = CornerSize(16.dp)),
            colors = TextFieldDefaults.run {
                textFieldColors(


                    focusedIndicatorColor = Color.Transparent, // Remove indicator color
                    unfocusedIndicatorColor = Color.Transparent // Remove indicator color
                )
            },


            label = { Text("Enter your message") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Button for addphoto
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                // fuction open image in phone
                onClick = {
                    launcher.launch("image/*")
                },
                colors = ButtonDefaults.buttonColors(backgroundButtonColor),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
                    .shadow(5.dp),
                shape = RoundedCornerShape(corner = CornerSize(8.dp))


            ) {
                Icon(
                    painter = painterResource(R.drawable.addpost),
                    contentDescription = "Edit Picture",
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Edit Picture",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }
        }
    }
}

//import android.annotation.SuppressLint
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.material.icons.filled.Done
//import androidx.compose.material3.Card
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import com.example.project_mobile.R
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@Composable
//fun EditPost(navController: NavHostController) {
//    val backgroundColor = Color(184, 236, 253, 255)
//    Column(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(60.dp)
//                .background(backgroundColor)
//                .border(0.5.dp, Color.Black),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            IconButton(onClick = { navController.popBackStack() }) {
//                Icon(
//                    Icons.Default.Close,
//                    contentDescription = "Back",
//                    modifier = Modifier.size(50.dp)
//                )
//            }
//
//
//            Text(
//                text = "EditPost",
//                style = MaterialTheme.typography.titleLarge,
//                fontSize = 30.sp
//            )
//
//
//            IconButton(onClick = { /* Handle post click */ }) {
//                Icon(
//                    Icons.Default.Done,
//                    contentDescription = "Post",
//                    modifier = Modifier.size(50.dp)
//                )
//            }
//        }
//
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = 16.dp),
//            verticalArrangement = Arrangement.Top,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Spacer(modifier = Modifier.height(96.dp))
//
//
//            Card(
//                shape = MaterialTheme.shapes.small,
//                modifier = Modifier
//                    .background(Color.White)
//                    .fillMaxWidth()
//                    .height(350.dp)
//            ) {
//                Image(
//                    painter = painterResource(R.drawable.ic_launcher_background),
//                    contentDescription = "แก้ไขโพสต์",
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(horizontal = 16.dp)
//                )
//            }
//            Spacer(modifier = Modifier.height(16.dp))
//
//
//            OutlinedTextField(
//                value = "",
//                onValueChange = {},
//                label = { Text("Caption") },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(100.dp)
//            )
//
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//
//                IconButton(onClick = { /* Handle edit photo click */ }) {
//                    Image(painter = painterResource(R.drawable.gallery),
//                        contentDescription = "Edit Photo",
//                        Modifier.size(35.dp))
//                }
//
//
//                Text(
//                    text = "Edit Picture",
//                    style = MaterialTheme.typography.bodyLarge,
//                    fontSize = 20.sp,
//                )
//            }
//        }
//    }
//}


