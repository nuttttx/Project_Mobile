package com.example.project_mobile.ui.theme

import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.ui.text.input.ImeAction
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    var userEmail by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf(-1) }
    var password by remember { mutableStateOf("") }
    var isButtonEnabled by remember { mutableStateOf(true) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val createClient = ChitChatAPI.create()
    val contextForToast = LocalContext.current.applicationContext

    lateinit var sharedPreferences: SharedPreferencesManager
    sharedPreferences = SharedPreferencesManager(context = contextForToast)

    // Check Lifecycle State
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    LaunchedEffect(lifecycleState) {
        when (lifecycleState) {
            Lifecycle.State.RESUMED -> {
                if (sharedPreferences.isLoggedIn) {
                    navController.navigate(Screen2.Profile.route)
                }

                if (sharedPreferences.userEmail.isNullOrEmpty()) {
                    userEmail = sharedPreferences.userEmail ?: ""
                }
            }
            else -> {}
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(255, 255, 255, 255))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
//            .background(color = Color(250, 226, 244, 255)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                text = "ChitChat",
                fontSize = 50.sp,
                color = Color(130, 0, 131, 255),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(60.dp))

            OutlinedTextField(
                value = userEmail,
                onValueChange = { userEmail = it },
                label = { Text(text = "Email") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(.9f)

            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null
                    )
                },
                modifier = Modifier.fillMaxWidth(.9f)
            )

            Spacer(modifier = Modifier.height(54.dp))

            Button(
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()

                    createClient.loginUser(userEmail, password)
                        .enqueue(object : Callback<LoginClass> {
                            override fun onResponse(
                                call: Call<LoginClass>,
                                response: Response<LoginClass>
                            ) {
                                if (response.isSuccessful) {
                                    if (response.body()?.success == 1) {
                                        sharedPreferences.isLoggedIn = true
                                        sharedPreferences.userEmail = response.body()?.email ?: ""
                                        sharedPreferences.userId = response.body()?.user_id
                                        Toast.makeText(
                                            contextForToast,
                                            "Login successful",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        navController.navigate(Screen2.Home.route)
                                    } else {
                                        Toast.makeText(
                                            contextForToast,
                                            "Email or password is incorrect.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        contextForToast,
                                        "Error occurred",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                            override fun onFailure(call: Call<LoginClass>, t: Throwable) {
                                Toast.makeText(
                                    contextForToast,
                                    "Error onFailure: " + t.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        })
                },
                enabled = isButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(Color(130, 0, 131, 255)),
                shape = RoundedCornerShape(5.dp)
            ) {
                Text(text = "Login",
                    fontSize = 16.sp,)
            }

            Spacer(modifier = Modifier.height(54.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .weight(1f)
                        .background(color = Color.Gray)
                )
                Text(
                    text = "or",
                    color = Color.Gray,
                    fontSize = 19.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .weight(1f)
                        .background(color = Color.Gray)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't have an account?",
                    modifier = Modifier.clickable {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        navController.navigate(Screen.Register.route)
                    }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    navController.navigate(Screen.Register.route)
                },
                enabled = isButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(Color(130, 0, 131, 255)),
                shape = RoundedCornerShape(5.dp)
            ) {
                Text(text = "Register",
                    fontSize = 16.sp,
                )
            }
        }
    }
}









