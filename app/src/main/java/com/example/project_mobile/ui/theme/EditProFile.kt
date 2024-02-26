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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.project_mobile.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Timestamp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavHostController) {
    val createClient = ChitChatAPI.create()
    lateinit var sharedPreferences: SharedPreferencesManager
    val contextForToast = LocalContext.current.applicationContext
    sharedPreferences = SharedPreferencesManager(contextForToast)
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

    var username by remember { mutableStateOf(data.user_name) }
    var email by remember { mutableStateOf(data.email) }
    var genderValue by remember { mutableStateOf(data.gender) }
    var img by remember { mutableStateOf(data.img) }




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
//                style = TextStyle(color = Color(130, 0, 131, 255))
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


        // รูปโปรไฟล์และแก้ไขข้อมูล
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 6.dp, bottom = 0.1.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                // รูปโปรไฟล์
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(data.img),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentScale = ContentScale.Crop
                    )
                }


            }
        }
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextButton(
                onClick = {
                    if (navController.currentBackStack.value.size >= 2) {
                        navController.popBackStack()
                    }
                    navController.navigate(Screen.ImageProfile.route)
                },
            ) {
                Text(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    text = "แก้ไขรูป"
                )
            }




            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("ชื่อผู้ใช้") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(corner = CornerSize(8.dp)),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(130, 0, 131, 255),
                    unfocusedBorderColor = Color(130, 0, 131, 255),
                    cursorColor = Color(130, 0, 131, 255),
                ),
            )

            genderValue = EditRadioGroupUsage(genderValue)

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("อีเมล") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(corner = CornerSize(8.dp)),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(130, 0, 131, 255),
                    unfocusedBorderColor = Color(130, 0, 131, 255),
                    cursorColor = Color(130, 0, 131, 255),
                ),


                )
            Spacer(Modifier.padding(10.dp))
            OutlinedTextField(
                value = img,
                onValueChange = { img = it },
                label = { Text("URL รูปภาพ") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                shape = RoundedCornerShape(corner = CornerSize(8.dp)),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(130, 0, 131, 255),
                    unfocusedBorderColor = Color(130, 0, 131, 255),
                    cursorColor = Color(130, 0, 131, 255),
                ),
            )
            Spacer(Modifier.padding(10.dp))
            Button(
                onClick = {
                    createClient.updateProfile(
                        data.user_id,
                        email = email,
                        gender = genderValue,
                        img =  img,
                        user_name = username,
                    ).enqueue(object : Callback<ProfileClass> {
                        override fun onResponse(call: Call<ProfileClass>, response: Response<ProfileClass>) {
                            if (response.isSuccessful) {
                                Toast.makeText(contextForToast, "Successfully Updated", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(contextForToast, "Update Failure", Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onFailure(call: Call<ProfileClass>, t: Throwable) {
                            Toast.makeText(contextForToast, "Error onFailure " + t.message, Toast.LENGTH_LONG).show()
                        }
                    }
                    )
                    navController.navigate(Screen.Profile.route)
                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(Color(130, 0, 131, 255)),
            ) {
                Text("บันทึก")
            }
        }
    }
}


@Composable
fun EditRadioGroupUsage(s: String): String {
    val kinds = listOf("Male", "Female", "Other")
    var (selected, setSelected) = remember { mutableStateOf(s) }


    Text(
        text = "Gender:",
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 10.dp),
    )


    Row(modifier = Modifier.fillMaxWidth().padding(start = 16.dp)) {
        EditRadioGroup(mItems = kinds, selected = selected, setSelected = setSelected)
    }


    return selected
}


@Composable
fun EditRadioGroup(
    mItems: List<String>,
    selected: String,
    setSelected: (selected: String) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        mItems.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selected == item,
                    onClick = {
                        setSelected(item)
                    },
                    enabled = true,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(130, 0, 131, 255)
                    )
                )
                Text(
                    text = item,
                    modifier = Modifier.padding(start = 5.dp)
                )
            }
        }
    }
}
