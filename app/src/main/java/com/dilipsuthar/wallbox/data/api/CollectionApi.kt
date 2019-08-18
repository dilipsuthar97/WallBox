package com.dilipsuthar.wallbox.data.api

import com.dilipsuthar.wallbox.data.model.Collection
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CollectionApi {

    /**
     * Api for get all collection list from server
     *
     * @param client_id Unsplash access key
     * @param page page id to load on every new request
     * @param per_page default count of collection on every request
     */
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

    /**
     * Api for fetch collection for given id
     *
     * @param id Id of collection which will be fetch
     */
    @GET("collections/{id}")
    fun getCollection(@Path("id") id: String,
                      @Query("client_id") client_id: String): Call<Collection>

}