package com.example.project_mobile.ui.theme

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
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
//ImageProfile



@Composable
fun ImageProfile(navController: NavHostController) {
    lateinit var sharedPreferences: SharedPreferencesManager
    val contextForToast = LocalContext.current.applicationContext
    sharedPreferences = SharedPreferencesManager(contextForToast)
    val userId = sharedPreferences.userId ?: 0
    val createClient = ChitChatAPI.create()

    val data = navController.previousBackStackEntry?.savedStateHandle?.get<ProfileClass>("data")
        ?: ProfileClass(
            0,
            "",
            "",
            "",
            "",
            Timestamp(0),
            Timestamp(0),
            0
        )

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                // Handle the selected image URI
                selectedImageUri = uri
            }
        }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        // ส่วนหัว
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 6.dp, top = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    navController.navigate(Screen.Profile.route)
                },
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "แก้ไขโปรไฟล์",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(end = 50.dp)
                    .weight(1f),
            )
        }


        // เส้นแบ่ง
        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))


        // รูปโปรไฟล์และแก้ไขข้อมูล
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 6.dp, bottom = 0.1.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                    painter = if(selectedImageUri != null)
                {
                    rememberAsyncImagePainter( selectedImageUri)
                }else{
                    rememberAsyncImagePainter(data.img)
                },
                    contentDescription = null,
                    modifier = Modifier.size(250.dp)
                        .align(Alignment.Center))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 6.dp, bottom = 0.1.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    launcher.launch("image/*")
                },
                colors = ButtonDefaults.buttonColors(Color(130, 0, 131, 255)),
            ) {
                Text("เปิดคลังรูปภาพ")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 6.dp, bottom = 0.1.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                //ปุ่มแก้ไข
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
                        imageFile.name, requestBody)

                    createClient.uploadProfile(
                        userId,
                        imagePart,// ส่งไฟล์ jpg

                    ).enqueue(object : Callback<ProfileClass> {
                        override fun onResponse(
                            call: Call<ProfileClass>,
                            response: Response<ProfileClass>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    contextForToast,
                                    "Successfully Inserted",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    contextForToast,
                                    "Error Inserted",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }


                        override fun onFailure(call: Call<ProfileClass>, t: Throwable) {
                            Toast.makeText(
                                contextForToast,
                                "Error onFailure " + t.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    })

                    if (navController.currentBackStack.value.size >= 2) {
                        navController.popBackStack()
                    }
                    navController.navigate(Screen.Profile.route)
                },
                colors = ButtonDefaults.buttonColors(Color(130, 0, 131, 255)),

            ) {
                Text("บันทึก")
            }
        }
    }
}
