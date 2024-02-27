package com.example.project_mobile.ui.theme

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.sql.Timestamp

@Parcelize
class LikesClass (
    val status: Int,
    val user_id: Int?,
    val post_id: Int?,
    val create_at: Timestamp,
    val update_at: Timestamp,
    val delete_at: Int
): Parcelable