package com.example.project_mobile.ui.theme

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FriendRequestClass(
    @Expose
    @SerializedName("request_id") val requestId: Int,

    @Expose
    @SerializedName("send_user_id") val sendUserId: Int,

    @Expose
    @SerializedName("receive_user_id") val receiveUserId: Int,

    @Expose
    @SerializedName("status") val status: Int,

    @Expose
    @SerializedName("create_at") val create_at: String,

    @Expose
    @SerializedName("delete_at") val delete_at: String,
)

