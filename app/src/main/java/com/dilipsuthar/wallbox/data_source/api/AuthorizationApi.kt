package com.dilipsuthar.wallbox.data_source.api

import com.dilipsuthar.wallbox.data_source.model.AccessToken
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthorizationApi {

    @POST("oauth/token")
    fun getAccessToken(@Query("client_id") client_id: String,
                       @Query("client_secret") client_secret: String,
                       @Query("redirect_uri") redirect_uri: String,
                       @Query("code") code: String,
                       @Query("grant_type") grant_type: String): Call<AccessToken>

}