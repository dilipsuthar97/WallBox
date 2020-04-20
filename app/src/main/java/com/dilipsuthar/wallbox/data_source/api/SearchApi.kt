package com.dilipsuthar.wallbox.data_source.api

import com.dilipsuthar.wallbox.data_source.model.SearchCollections
import com.dilipsuthar.wallbox.data_source.model.SearchPhotos
import com.dilipsuthar.wallbox.data_source.model.SearchUsers
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET("search/photos")
    fun searchPhotos(@Query("query") query: String,
                     @Query("page") page: Int,
                     @Query("per_page") per_page: Int,
                     @Query("collections") collections: String?): Call<SearchPhotos>

    @GET("search/collections")
    fun searchCollections(@Query("query") query: String,
                          @Query("page") page: Int,
                          @Query("per_page") per_page: Int): Call<SearchCollections>

    @GET("search/users")
    fun searchUsers(@Query("query") query: String,
                    @Query("page") page: Int,
                    @Query("per_page") per_page: Int): Call<SearchUsers>

}