package com.example.project_mobile.ui.theme

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class CommentClass(
    @Expose
    @SerializedName("comment_id") val comment_id: Int,
    @Expose
    @SerializedName("text") val text: String,
    @Expose
    @SerializedName("post_id") val post_id: Int,
    @Expose
    @SerializedName("create_at") val created_at: Timestamp,
    @Expose
    @SerializedName("delete_at") val deleted_at: Int,
    @Expose
    @SerializedName("user_name") val userName: String,
    @Expose
    @SerializedName("img") val img: String,
)
