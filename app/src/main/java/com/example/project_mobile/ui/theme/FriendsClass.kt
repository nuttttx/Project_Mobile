package com.example.project_mobile.ui.theme

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.sql.Timestamp

//Nut
@Parcelize
class FriendsClass (
    val user_id: Int,
    val user_name: String,
    val email: String,
    val img: String,
    val gender: String,
    val create_at: Timestamp,
    val update_at: Timestamp,
    val delete_at: Int,
): Parcelable
