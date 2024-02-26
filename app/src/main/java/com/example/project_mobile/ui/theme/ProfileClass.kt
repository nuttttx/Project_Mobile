package com.example.project_mobile.ui.theme

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.sql.Timestamp

@Parcelize
data class ProfileClass(
    val user_id: Int,
    val user_name: String,
    val email: String,
    val img: String,
    val gender: String,
    val create_at: Timestamp,
    val update_at: Timestamp,
    val delete_at: Int,
): Parcelable

