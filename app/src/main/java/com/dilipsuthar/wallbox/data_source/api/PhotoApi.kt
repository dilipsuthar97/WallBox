package com.dilipsuthar.wallbox.data_source.api

import com.dilipsuthar.wallbox.data_source.model.Photo
import com.dilipsuthar.wallbox.data_source.model.PhotoStatistics
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
    fun getPhotos(@Query("page") page: Int,
                  @Query("per_page") per_page: Int,
                  @Query("order_by") order_by: String): Call<List<Photo>>

    @GET("photos/curated")
    fun getCuratedPhotos(@Query("page") page: Int,
                         @Query("per_page") per_page: Int,
                         @Query("order_by") order_by: String): Call<List<Photo>>

    @GET("photos/{id}")
    fun getPhoto(@Path("id") id: String): Call<Photo>

    @GET("photos/{id}/statistics")
    fun getPhotoStatistics(@Path("id") id: String): Call<PhotoStatistics>

    @GET("collections/{id}/photos")
    fun getCollectionPhotos(@Path("id") id: String,
                            @Query("page") page: Int,
                            @Query("per_page") per_page: Int): Call<List<Photo>>

    @GET("collections/curated/{id}/photos")
    fun getCuratedCollectionPhotos(@Path("id") id: String,
                                   @Query("page") page: Int,
                                   @Query("per_page") per_page: Int): Call<List<Photo>>

    @GET("users/{username}/photos")
    fun getUserPhotos(@Path("username") username: String,
                      @Query("page") page: Int,
                      @Query("per_page") per_page: Int,
                      @Query("order_by") order_by: String): Call<List<Photo>>

    @GET("users/{username}/likes")
    fun getUserLikedPhotos(@Path("username") username: String,
                           @Query("page") page: Int,
                           @Query("per_page") per_page: Int,
                           @Query("order_by") order_by: String): Call<List<Photo>>
}