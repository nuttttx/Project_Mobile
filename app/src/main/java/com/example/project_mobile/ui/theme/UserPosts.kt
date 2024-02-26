package com.example.project_mobile.ui.theme

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.sql.Timestamp

@Parcelize
class UserPosts(
    val post_id: Int,
    val text: String,
    val img: String,
    val user_id: Int,
    val create_at: Timestamp,
    val update_at: Timestamp,
    val delete_at: Int,
    val user_name: String,
    val user_img: String,
    val comment_count: Int,
    val like_count: Int,
): Parcelable






