package com.dilipsuthar.wallbox.data.api

import com.dilipsuthar.wallbox.data.model.Photo
import com.dilipsuthar.wallbox.data.model.PhotoStatistics
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PhotoApi {

    /** Test API */
    @GET("14jmjx")
    fun getTestPhotos(): Call<List<Photo>>

    /** Main API's */
    @GET("photos")
    fun getPhotos(@Query("client_id") client_id: String,
                  @Query("page") page: Int,
                  @Query("per_page") per_page: Int,
                  @Query("order_by") order_by: String): Call<List<Photo>>

    @GET("photos/curated")
    fun getCuratedPhotos(@Query("client_id") client_id: String,
                         @Query("page") page: Int,
                         @Query("per_page") per_page: Int,
                         @Query("order_by") order_by: String): Call<List<Photo>>

    @GET("photos/{id}")
    fun getPhoto(@Path("id") id: String,
                 @Query("client_id") client_id: String): Call<Photo>

    @GET("photos/{id}/statistics")
    fun getPhotoStatistics(@Path("id") id: String,
                           @Query("client_id") client_id: String): Call<PhotoStatistics>
}