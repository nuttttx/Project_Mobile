package com.example.project_mobile.ui.theme

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Timestamp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllUserClass(navController: NavHostController) {
    val createClient = ChitChatAPI.create()
    var UserItemsList = remember { mutableStateListOf<AllUserClass>() }
    val contextForToast = LocalContext.current.applicationContext
//    var textFildeID by remember { mutableStateOf("") }


    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    LaunchedEffect(lifecycleState){
        when (lifecycleState){
            Lifecycle.State.DESTROYED -> {}
            Lifecycle.State.INITIALIZED -> {}
            Lifecycle.State.CREATED -> {}
            Lifecycle.State.STARTED -> {}
            Lifecycle.State.RESUMED -> {
                showAllData(UserItemsList, contextForToast)
            }
        }
    }

    UserItemsList.clear()

    createClient.retrieveUser().enqueue(object : Callback<List<AllUserClass>> {
        override fun onResponse(call: Call<List<AllUserClass>>, response: Response<List<AllUserClass>>) {
            response.body()?.forEach {
                UserItemsList.add(AllUserClass(it.user_id, it.user_name, it.email, it.password,it.img,it.gender,  it.create_at, it.update_at, it.delete_at))

            }
        }

        override fun onFailure(call: Call<List<AllUserClass>>, t: Throwable) {
            Toast.makeText(
                contextForToast,
                "Error onFailure: ${t.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    })

    Column{
        Row(
            Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
//            Text(
//                text = "Search:",
//                fontSize = 20.sp
//            )
//
//            OutlinedTextField(
//                modifier = Modifier
//                    .width(230.dp)
//                    .padding(10.dp),
//                value = textFildeID,
//                onValueChange = {textFildeID = it},
//                label = { Text("Student ID") }
//            )
//
//            Button(onClick = {
//                if(textFildeID.trim().isEmpty()){
//                    showAllData(studentItemsList, contextForToast)
//                }
//
//            })
//            {
//                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
//            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(0.85f)) {
                Text(
                    text = "Student Lists:",
                    fontSize = 25.sp
                )
            }

            Button(
                onClick = {
                    if (navController.currentBackStack.value.size >= 2) {
                        navController.popBackStack()
                    }
                    navController.navigate(Screen2.Profile.route)
                }
            ) {
                Text(text = "Back To Profile")
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
//            var itemClick = AllUserClass("", "", "", "")
            var itemClick = AllUserClass(0, "", "", "", "", "", Timestamp(0), Timestamp(0), Timestamp(0))
            itemsIndexed(
                items = UserItemsList
            ) { index, item ->
                Card(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .fillMaxWidth()
                        .height(130.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    ),
                    shape = RoundedCornerShape(corner = CornerSize(16.dp)),
                    onClick = {
                        Toast.makeText(
                            contextForToast,
                            "Click on ${item.user_name}.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dp(value = 130f))
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween

                    ) {
                        Text(
                            text = "ID: ${item.user_id}\n" +
                                    "Name: ${item.user_name}\n" +
                                    "Email: ${item.email}\n" +
                                    "Password: ${item.password}\n" +
                                    "Image: ${item.img}\n" +
                                    "Gender: ${item.gender}\n"
//                                    "Role: ${item.role}"
                        )

                        TextButton(onClick = {
                            itemClick = item
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "data",
                                AllUserClass(
                                    item.user_id,
                                    item.user_name,
                                    item.email,
                                    item.password,
                                    item.img,
                                    item.gender,
                                    item.create_at,
                                    item.update_at,
                                    item.delete_at
                                )
                            )
//                            navController.navigate(Screen.Edit.route)
                        }
                        )
                        {
//                            Text("Edit/Delete")
                        }
                    }
                }
            }
        }
    }
}

fun showAllData(studentItemsList: MutableList<AllUserClass>, context: Context) {
    val createClient =  ChitChatAPI.create()
    createClient.retrieveUser().enqueue(object : Callback<List<AllUserClass>> {
        override fun onResponse(call: Call<List<AllUserClass>>, response: Response<List<AllUserClass>>) {
            studentItemsList.clear()
            response.body()?.forEach {
                studentItemsList.add(AllUserClass(it.user_id, it.user_name, it.email, it.password,it.img,it.gender,  it.create_at, it.update_at, it.delete_at))
            }
        }

        override fun onFailure(call: Call<List<AllUserClass>>, t: Throwable) {
            Toast.makeText(context, "Error onFailure: " + t.message, Toast.LENGTH_LONG).show()
        }
    })
}