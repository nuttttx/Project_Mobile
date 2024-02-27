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
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.mutableIntStateOf
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
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.project_mobile.R
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    val createClient = ChitChatAPI.create()
    val backgroundColor = Color(130, 0, 131, 255)
    val backgroundButtonColor = Color(130, 0, 131, 255)

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                // Handle the selected image URI
                selectedImageUri = uri
            }
        }
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    val post = navController.previousBackStackEntry?.savedStateHandle?.get<PostClass>("post")
        ?: PostClass(
            0,
            "",
            "",
            0,
            Timestamp(0),
            Timestamp(0),
            0,
            "",
            "",
            0,
            0,
            0
        )
    var caption by remember { mutableStateOf(post.text) }
    var postId by remember { mutableIntStateOf(post.post_id) }
//    Toast.makeText(contextForToast, caption, Toast.LENGTH_LONG).show()
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
                    val inputStream =
                        contextForToast.contentResolver.openInputStream(selectedImageUri!!)
                            ?: throw Exception("Failed to open input stream")
                    val imageFile = File.createTempFile("image", ".jpg")
                    val outputStream = FileOutputStream(imageFile)
                    inputStream.copyTo(outputStream)
                    inputStream.close()
                    outputStream.close()

                    val requestBody = imageFile.asRequestBody("img/jpeg".toMediaTypeOrNull())
                    val imagePart = MultipartBody.Part.createFormData(
                        "img",
                        imageFile.name, requestBody
                    )
                    val captionRequestBody = caption.toRequestBody("text/plain".toMediaTypeOrNull())

                    Toast.makeText(contextForToast, caption, Toast.LENGTH_LONG).show()

                    createClient.editPost(
                        postId,
//                        captionRequestBody,
                        imagePart,


                        ).enqueue(object : Callback<PostClass> {
                        override fun onResponse(
                            call: Call<PostClass>,
                            response: Response<PostClass>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    contextForToast,
                                    "Successfully Updated",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                Toast.makeText(contextForToast, "Update Failure", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }

                        override fun onFailure(call: Call<PostClass>, t: Throwable) {
                            Toast.makeText(
                                contextForToast,
                                "Error onFailure " + t.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    )
                    navController.navigate(Screen.Profile.route)
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


            Box(
                modifier = Modifier
                    .size(300.dp)
                    .padding(vertical = 16.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = if (selectedImageUri != null) {
                        rememberAsyncImagePainter(selectedImageUri)
                    } else {
                        rememberAsyncImagePainter(post.img)
                    },
                    contentDescription = null,
                    modifier = Modifier
                        .size(250.dp)
                        .align(Alignment.Center)
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
            enabled = false,

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
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                //ลบโพสต์
                onClick = {
                    createClient.deletePost(postId).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    contextForToast,
                                    "Successfully Delete",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                Toast.makeText(
                                    contextForToast,
                                    "Fail Delete",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(
                                contextForToast,
                                "Error onFailure " + t.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })

                },
                colors = ButtonDefaults.buttonColors(Color.Red),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
                    .shadow(5.dp),
                shape = RoundedCornerShape(corner = CornerSize(8.dp))


            ) {
                Text(
                    text = "Delete Post",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }
        }

    }
}




