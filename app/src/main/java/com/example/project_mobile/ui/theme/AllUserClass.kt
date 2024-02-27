package com.example.project_mobile.ui.theme

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.sql.Timestamp


@Parcelize
data class AllUserClass(

    @Expose
    @SerializedName("user_id") val user_id: Int,

    @Expose
    @SerializedName("user_name") val user_name: String,

    @Expose
    @SerializedName("email") val email: String,

    @Expose
    @SerializedName("password") val password: String,

    @Expose
    @SerializedName("img") val img: String,

    @Expose
    @SerializedName("gender") val gender: String,

    @Expose
    @SerializedName("create_at") val create_at: Timestamp,

    @Expose
    @SerializedName("update_at") val update_at: Timestamp,

    @Expose
    @SerializedName("delete_at") val delete_at: Int,

    ):Parcelable