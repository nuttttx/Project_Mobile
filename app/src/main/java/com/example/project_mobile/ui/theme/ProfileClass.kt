package com.example.project_mobile.ui.theme

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileClass(
    @Expose
    @SerializedName("user_id") val user_id: Int,
    @Expose
    @SerializedName("user_name") val user_name: String,
    @Expose
    @SerializedName("img") val img: String,
): Parcelable
