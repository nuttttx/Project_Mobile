package com.example.project_mobile.ui.theme

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

//data class AcceptFriendClass(
//    @SerializedName("request_id") val request_id: Int,
//
//    @SerializedName("send_user_id") val sendUserId: Int,
//
//    @SerializedName("receive_user_id") val receiveUserId: Int,
//
//    @SerializedName("user_name") val user_name: String,
//
//    @SerializedName("create_at") val create_at: String,
//
//    @SerializedName("img") val img: String,
//
//    @SerializedName("gender") val gender: String,
//)
data class AcceptFriendClass(
    @Expose
    @SerializedName("success") val success: Boolean,

    @Expose
    @SerializedName("message") val message: String
)