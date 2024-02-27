package com.example.project_mobile.ui.theme

import com.google.gson.annotations.SerializedName

data class FriendRequestData(

    @SerializedName("send_user_id") val sendUserId: Int,

    @SerializedName("receive_user_id") val receiveUserId: Int,

)
