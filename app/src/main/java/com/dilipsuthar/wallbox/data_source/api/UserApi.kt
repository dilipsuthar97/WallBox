package com.dilipsuthar.wallbox.data_source.api

import com.dilipsuthar.wallbox.data_source.model.Me
import com.dilipsuthar.wallbox.data_source.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {

    @GET("users/{username}")
    fun getUserProfile(@Path("username") username: String): Call<User>

    @GET("me")
    fun getMeProfile(): Call<Me>

}