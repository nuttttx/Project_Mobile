package com.example.project_mobile.ui.theme
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChitChatAPI {

    @GET("allUser")
    fun retrieveUser(): Call<List<AllUserClass>>

    @GET("login/{email}/{password}")
    fun loginUser(
        @Path("email") email: String,
        @Path("password") password: String
    ): Call<LoginClass>

    @GET("search/{user_name}")
    fun searchUser(
        @Path("user_name") user_name: String
    ): Call<AllUserClass>

    @FormUrlEncoded
    @POST("insertAccount")
    fun registerUser(
        @Field("user_name") user_name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("img") img: String,
        @Field("gender") gender: String
    ): Call<LoginClass>

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
