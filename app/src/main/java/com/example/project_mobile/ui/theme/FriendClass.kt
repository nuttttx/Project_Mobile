package com.example.project_mobile.ui.theme

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FriendClass(
    @Expose
    @SerializedName("request_id") val requestId: Int,
    @Expose
    @SerializedName("user_id")  val user_id: Int,
    @Expose
    @SerializedName("user_name") val user_name: String,
    @Expose
    @SerializedName("gender") val gender: String,
    @Expose
    @SerializedName("img") val img: String,

    @Expose
    @SerializedName("send_user_id") val sendUserId: Int,

    @Expose
    @SerializedName("receive_user_id") val receiveUserId: Int,

)
