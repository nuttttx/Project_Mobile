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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun RegisterScreen(navController: NavHostController) {
    val contextForToast = LocalContext.current
    val createClient = ChitChatAPI.create()
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }

    var img by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isButtonEnabled by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

//    lateinit var sharedPreferences: SharedPreferencesManager
//    sharedPreferences = SharedPreferencesManager(context = contextForToast)




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
            Spacer(modifier = Modifier.height(35.dp))
            Text(
                text = "ChitChat",
                fontSize = 50.sp,
                color = Color(130, 0, 131, 255),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(35.dp))
            OutlinedTextField(
                value = userName,
                onValueChange = { newUserName -> // Specify parameter name explicitly
                    userName = newUserName

                },
                label = { Text("Username") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
//                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null
                    )
                },
                modifier = Modifier.fillMaxWidth(.9f)
            )
            Spacer(modifier = Modifier.height(16.dp))
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
                onValueChange = { newPassword -> // Specify parameter name explicitly
                    password = newPassword
                    isButtonEnabled = validateInput(userEmail, password)
                },
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
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
            Spacer(modifier = Modifier.height(16.dp))

            selectedGender = RadioGroupUsage ()
            Spacer(modifier = Modifier.height(40.dp))


            Button(
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    createClient.registerUser(
                        userName ,
                        userEmail,
                        password,
                        selectedGender,
                        img ,

                    ).enqueue(object : Callback<LoginClass> {
                        override fun onResponse(call: Call<LoginClass>, response: Response<LoginClass>) {
                            if (response.isSuccessful) {
                                Toast.makeText(contextForToast, "Successfully Inserted", Toast.LENGTH_SHORT).show()
                                if (navController.currentBackStack.value.size >= 2) {
                                    navController.popBackStack()
                                }
                                navController.navigate(Screen.Login.route)
                            } else {
                                Toast.makeText(contextForToast, "Inserted Failed", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<LoginClass>, t: Throwable) {
                            Toast.makeText(contextForToast, "Error onFailure " + t.message, Toast.LENGTH_LONG).show()
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
                Text(text = "Register",
                    fontSize = 16.sp,)
            }

            Spacer(modifier = Modifier.height(40.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .weight(0.7f)
                        .background(color = Color.Gray)
                )
                Text(
                    text = "or",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .weight(0.7f)
                        .background(color = Color.Gray)
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Have an account?",
                    modifier = Modifier.clickable {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        navController.navigate(Screen.Register.route)
                    }
                )
            }
            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    navController.navigate(Screen.Login.route)
                },
//                enabled = isButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(Color(130, 0, 131, 255)),
                shape = RoundedCornerShape(5.dp)
            ) {
                Text(text = "Login",
                    fontSize = 16.sp,)
            }
        }
    }
}
fun validateInput(email: String, password: String): Boolean =
    !email.isNullOrEmpty() && !password.isNullOrEmpty()


@Composable
fun MyRadioGroup(
    mItems: List<String>,
    selected: String,
    setSelected: (selected: String) -> Unit,
) {

    Row(
        modifier = Modifier.fillMaxWidth(0.9f).background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
        ) {
        mItems.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                RadioButton(
                    selected = selected == item,
                    onClick = { setSelected(item) },
                    enabled = true,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Magenta
                    )
                )
                Text(text = item,
                    modifier = Modifier.padding(start = 0.5.dp),
                    style = TextStyle(fontSize = 12.sp)
                )
            }
        }
    }
}

@Composable
fun RadioGroupUsage(): String {
    val kinds = listOf("Male", "Female", "Other")
    var (selected, setSelected) = remember { mutableStateOf("") }


    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Gender: $selected",
            textAlign = TextAlign.Start,
            modifier = Modifier

                .fillMaxWidth()
                .padding(start = 20.dp, top = 10.dp),
            style = TextStyle(
                fontSize = 16.sp
            )

        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
                .align(Alignment.Start),
            verticalAlignment = Alignment.CenterVertically ,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MyRadioGroup(
                mItems = kinds,
                selected = selected,
                setSelected = setSelected
            )
        }
    }

    return selected
}