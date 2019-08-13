package com.dilipsuthar.wallbox.data.service

import android.os.AsyncTask
import com.dilipsuthar.wallbox.BuildConfig
import com.dilipsuthar.wallbox.WallBox
import com.dilipsuthar.wallbox.data.api.PhotoApi
import com.dilipsuthar.wallbox.data.model.Photo
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Log
import com.dilipsuthar.wallbox.data.api.CollectionApi
import com.dilipsuthar.wallbox.data.model.Collection
import com.dilipsuthar.wallbox.data.model.PhotoStatistics

/**
 * Created by DILIP SUTHAR on 20/07/19
 * */

class Services {

    private var photosCall: Call<List<Photo>>? = null
    private var collectionsCall: Call<List<Collection>>? = null

    /** Photo services */
    fun requestPhotos(page: Int, per_page: Int, order_by: String, listener: OnRequestPhotosListener?) {
        val requestCall = buildApi(buildClient(), PhotoApi::class.java).getPhotos(BuildConfig.WALLBOX_ACCESS_KEY, page, per_page, order_by)
        requestCall.enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                listener?.onRequestPhotosSuccess(call, response)
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                listener?.onRequestPhotosFailed(call, t)
            }
        })
        photosCall = requestCall
    }

    fun requestCuratedPhotos(page: Int, per_page: Int, order_by: String, listener: OnRequestPhotosListener?) {
        val requestCall = buildApi(buildClient(), PhotoApi::class.java).getCuratedPhotos(BuildConfig.WALLBOX_ACCESS_KEY, page, per_page, order_by)
        requestCall.enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                listener?.onRequestPhotosSuccess(call, response)
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                listener?.onRequestPhotosFailed(call, t)
            }
        })
        photosCall = requestCall
    }

    fun requestPhoto(id: String, listener: OnRequestPhotoListener?) {
        val request = buildApi(buildClient(), PhotoApi::class.java).getPhoto(id, BuildConfig.WALLBOX_ACCESS_KEY)
        request.enqueue(object : Callback<Photo> {
            override fun onResponse(call: Call<Photo>, response: Response<Photo>) {
                listener?.onRequestPhotoSuccess(call, response)
            }

            override fun onFailure(call: Call<Photo>, t: Throwable) {
                listener?.onRequestPhotoFailed(call, t)
            }
        })
    }

    fun requestPhotoStatistics(id: String, listener: OnRequestPhotoStatistics?) {
        val request = buildApi(buildClient(), PhotoApi::class.java).getPhotoStatistics(id, BuildConfig.WALLBOX_ACCESS_KEY)
        request.enqueue(object : Callback<PhotoStatistics> {
            override fun onResponse(call: Call<PhotoStatistics>, response: Response<PhotoStatistics>) {
                listener?.onRequestSuccess(call, response)
            }

            override fun onFailure(call: Call<PhotoStatistics>, t: Throwable) {
                listener?.onRequestFailed(call, t)
            }
        })
    }


    /** Collection services*/
    fun requestCollections(page: Int, per_page: Int, listener: OnRequestCollectionsListener?) {
        Log.d(TAG, "requestCollections: called >>>>>>>>>> Services")

        val requestCall = buildApi(buildClient(), CollectionApi::class.java).getCollections(BuildConfig.WALLBOX_ACCESS_KEY, page, per_page)
        requestCall.enqueue(object : Callback<List<Collection>> {
            override fun onResponse(call: Call<List<Collection>>, response: Response<List<Collection>>) {
                listener?.onRequestCollectionsSuccess(call, response)
            }

            override fun onFailure(call: Call<List<Collection>>, t: Throwable) {
                listener?.onRequestCollectionsFailed(call, t)
            }
        })
        collectionsCall = requestCall
    }

    fun requestFeaturedCollections(page: Int, per_page: Int, listener: OnRequestCollectionsListener?) {
        Log.d(TAG, "requestFeaturedCollections: called >>>>>>>>>> Services")

        val requestCall = buildApi(buildClient(), CollectionApi::class.java).getFeaturedCollections(BuildConfig.WALLBOX_ACCESS_KEY, page, per_page)
        requestCall.enqueue(object : Callback<List<Collection>> {
            override fun onResponse(call: Call<List<Collection>>, response: Response<List<Collection>>) {
                listener?.onRequestCollectionsSuccess(call, response)
            }

            override fun onFailure(call: Call<List<Collection>>, t: Throwable) {
                listener?.onRequestCollectionsFailed(call, t)
            }
        })
        collectionsCall = requestCall
    }

    fun requestCuratedCollections(page: Int, per_page: Int, listener: OnRequestCollectionsListener?) {
        Log.d(TAG, "requestCuratedCollections: called >>>>>>>>>> Services")

        val requestCall = buildApi(buildClient(), CollectionApi::class.java).getCuratedCollections(BuildConfig.WALLBOX_ACCESS_KEY, page, per_page)
        requestCall.enqueue(object : Callback<List<Collection>> {
            override fun onResponse(call: Call<List<Collection>>, response: Response<List<Collection>>) {
                listener?.onRequestCollectionsSuccess(call, response)
            }

            override fun onFailure(call: Call<List<Collection>>, t: Throwable) {
                listener?.onRequestCollectionsFailed(call, t)
            }
        })
        collectionsCall = requestCall
    }

    /** TODO: Authorization service */

    fun cancel() {
        if (photosCall != null) photosCall?.cancel()
        if (collectionsCall != null) collectionsCall?.cancel()
    }

    /** build. */
    // Create/build okHttp client and return it ---->>>
    private fun buildClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .build()
    }

    // Create/build Retrofit API client and return it ---->>>
    private fun <T> buildApi(okHttpClient: OkHttpClient, Api: Class<T>): T {
        return Retrofit.Builder()
            .baseUrl(WallBox.UNSPLASH_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api)
    }

    // Text API builder ---->>>>
    private fun buildTestApi(okHttpClient: OkHttpClient): PhotoApi {
        return Retrofit.Builder()
            .baseUrl("https://api.myjson.com/bins/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PhotoApi::class.java)
    }

    /** interface */
    // Photo listener
    interface OnRequestPhotosListener {
        fun onRequestPhotosSuccess(call: Call<List<Photo>>, response: Response<List<Photo>>)
        fun onRequestPhotosFailed(call: Call<List<Photo>>, t: Throwable)
    }
    interface OnRequestPhotoListener {
        fun onRequestPhotoSuccess(call: Call<Photo>, response: Response<Photo>)
        fun onRequestPhotoFailed(call: Call<Photo>, t: Throwable)
    }
    interface OnRequestPhotoStatistics {
        fun onRequestSuccess(call: Call<PhotoStatistics>, response: Response<PhotoStatistics>)
        fun onRequestFailed(call: Call<PhotoStatistics>, t: Throwable)
    }

    // Collection listener
    interface OnRequestCollectionsListener {
        fun onRequestCollectionsSuccess(call: Call<List<Collection>>, response: Response<List<Collection>>)
        fun onRequestCollectionsFailed(call: Call<List<Collection>>, t: Throwable)
    }

    /** Singleton pattern */
    companion object {
        const val TAG = "WallBox.Services"
        private var instance: Services? = null

        fun getService(): Services {
            if (instance == null)
                instance = Services()

            return instance as Services
        }

    }

    /** AsyncTask class */
    class doAsync(private val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            handler
            return null
        }
    }

}