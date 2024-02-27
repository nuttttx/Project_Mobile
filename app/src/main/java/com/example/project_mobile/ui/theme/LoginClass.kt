package com.example.project_mobile.ui.theme

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class
LoginClass(
    @Expose
    @SerializedName("user_id") val user_id: Int,

    @Expose
    @SerializedName("success") val success: Int,


    @Expose
    @SerializedName("email") val email: String,


    )