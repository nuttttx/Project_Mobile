package com.example.project_mobile.ui.theme
import androidx.compose.runtime.snapshots.SnapshotStateList
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ChitChatAPI {

    @GET("allUser")
    fun retrieveUser(): Call<List<AllUserClass>>

    @FormUrlEncoded
    @POST("insertAccount")
    fun registerUser(
        @Field("user_name") user_name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("gender") gender: String,
        @Field("img") img: String,
        ): Call<LoginClass>

    @GET("login/{email}/{password}")
    fun loginUser(
        @Path("email") email: String,
        @Path("password") password: String
    ): Call<LoginClass>

//    @GET("search/{user_name}")
//    fun searchUser(
//        @Path("user_name") user_name: String
//    ): Call<List<LoginClass>>

    @GET("search/{user_name}")
    fun searchUser(
        @Path("user_name") user_name: String
    ): Call<AllUserClass>

    @GET("posts/{user_id}")
    fun getPosts(
        @Path("user_id") userId: Int
    ): Call<List<PostClass>>
    @GET("user/{user_id}")
    fun getUser(
        @Path("user_id") userId: Int
    ): Call<AllUserClass>

    @Multipart
    @POST("editPost/{post_id}")
    fun editPost(
        @Path("post_id") postId: Int,
        @Part("text") text: MultipartBody.Part,
        @Part imagePart: Int,
        @Part("user_id") userId: Int,
    ): Call<PostClass>

    @Multipart
    @POST("uploadPosts")
    fun uploadPost(
        @Part("text") text: RequestBody,
        @Part imagePart: MultipartBody.Part,
        @Part("user_id") userId: RequestBody
    ): Call<PostClass>

    @FormUrlEncoded
    @POST("comment")
    fun insertComment(
        @Field("text") text: String,
        @Field("user_id") userId: Int,
        @Field("post_id") postId: Int
    ): Call<CommentClass>


    @GET("comment/{post_id}")
    fun getComment(
        @Path("post_id") postId: Int
    ): Call<List<CommentClass>>











    companion object {
        fun create(): ChitChatAPI {
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ChitChatAPI::class.java)
        }
    }
}
