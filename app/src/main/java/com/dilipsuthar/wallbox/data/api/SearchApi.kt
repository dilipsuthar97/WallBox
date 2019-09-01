package com.dilipsuthar.wallbox.data.api

import com.dilipsuthar.wallbox.data.model.Photo
import com.dilipsuthar.wallbox.data.model.SearchCollections
import com.dilipsuthar.wallbox.data.model.SearchPhotos
import com.dilipsuthar.wallbox.data.model.SearchUsers
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET("search/photos")
    fun searchPhotos(@Query("client_id") client_id: String,
                     @Query("query") query: String,
                     @Query("page") page: Int,
                     @Query("per_page") per_page: Int,
                     @Query("collections") collections: String?): Call<SearchPhotos>

    @GET("search/collections")
    fun searchCollections(@Query("client_id") client_id: String,
                          @Query("query") query: String,
                          @Query("page") page: Int,
                          @Query("per_page") per_page: Int): Call<SearchCollections>

    @GET("search/users")
    fun searchUsers(@Query("client_id") client_id: String,
                    @Query("query") query: String,
                    @Query("page") page: Int,
                    @Query("per_page") per_page: Int): Call<SearchUsers>

}