package com.dilipsuthar.wallbox.data.api

import com.dilipsuthar.wallbox.data.model.Collection
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CollectionApi {

    @GET("collections")
    fun getCollections(@Query("client_id") client_id: String,
                       @Query("page") page: Int,
                       @Query("per_page") per_page: Int): Call<List<Collection>>

    @GET("collections/featured")
    fun getFeaturedCollections(@Query("client_id") client_id: String,
                       @Query("page") page: Int,
                       @Query("per_page") per_page: Int): Call<List<Collection>>

    @GET("collections/curated")
    fun getCuratedCollections(@Query("client_id") client_id: String,
                       @Query("page") page: Int,
                       @Query("per_page") per_page: Int): Call<List<Collection>>

}