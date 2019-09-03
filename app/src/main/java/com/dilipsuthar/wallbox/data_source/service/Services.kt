package com.dilipsuthar.wallbox.data_source.service

import android.os.AsyncTask
import com.dilipsuthar.wallbox.BuildConfig
import com.dilipsuthar.wallbox.WallBox
import com.dilipsuthar.wallbox.data_source.api.PhotoApi
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Log
import com.dilipsuthar.wallbox.data_source.api.CollectionApi
import com.dilipsuthar.wallbox.data_source.api.SearchApi
import com.dilipsuthar.wallbox.data_source.api.UserApi
import com.dilipsuthar.wallbox.data_source.model.*
import com.dilipsuthar.wallbox.data_source.model.Collection

/**
 * Created by DILIP SUTHAR on 20/07/19
 * */

class Services {

    private var photosCall: Call<List<Photo>>? = null
    private var collectionsCall: Call<List<Collection>>? = null
    private var photoCall: Call<Photo>? = null
    private var photoStatisticsCall: Call<PhotoStatistics>? = null

    /** Photo services ---------------------------------------------------------------------------------------------- */
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
        photoCall = request
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
        photoStatisticsCall = request
    }

    fun requestCollectionPhotos(id: String, page: Int, per_page: Int, listener: OnRequestPhotosListener?) {
        val request = buildApi(buildClient(), PhotoApi::class.java)
            .getCollectionPhotos(id, BuildConfig.WALLBOX_ACCESS_KEY, page, per_page)

        request.enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                listener?.onRequestPhotosSuccess(call, response)
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                listener?.onRequestPhotosFailed(call, t)
            }
        })
    }

    fun requestCuratedCollectionPhotos(id: String, page: Int, per_page: Int, listener: OnRequestPhotosListener?) {
        val request = buildApi(buildClient(), PhotoApi::class.java)
            .getCuratedCollectionPhotos(id, BuildConfig.WALLBOX_ACCESS_KEY, page, per_page)

        request.enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                listener?.onRequestPhotosSuccess(call, response)
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                listener?.onRequestPhotosFailed(call, t)
            }
        })
    }

    fun requestUserPhotos(user_name: String, page: Int, per_page: Int, order_by: String, listener: OnRequestPhotosListener?) {
        val request = buildApi(buildClient(), PhotoApi::class.java)
            .getUserPhotos(user_name, BuildConfig.WALLBOX_ACCESS_KEY, page, per_page, order_by)

        request.enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                listener?.onRequestPhotosSuccess(call, response)
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                listener?.onRequestPhotosFailed(call, t)
            }
        })
    }

    fun requestUserLikedPhotos(user_name: String, page: Int, per_page: Int, order_by: String, listener: OnRequestPhotosListener?) {
        val request = buildApi(buildClient(), PhotoApi::class.java)
            .getUserLikedPhotos(user_name, BuildConfig.WALLBOX_ACCESS_KEY, page, per_page, order_by)

        request.enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                listener?.onRequestPhotosSuccess(call, response)
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                listener?.onRequestPhotosFailed(call, t)
            }
        })
    }


    /** Collection services ----------------------------------------------------------------------------------------- */
    fun requestCollections(page: Int, per_page: Int, listener: OnRequestCollectionsListener?) {
        Log.d(TAG, "requestCollections: called >>>>>>>>>> Services")

        val requestCall = buildApi(buildClient(), CollectionApi::class.java)
            .getCollections(BuildConfig.WALLBOX_ACCESS_KEY, page, per_page)
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

        val requestCall = buildApi(buildClient(), CollectionApi::class.java)
            .getFeaturedCollections(BuildConfig.WALLBOX_ACCESS_KEY, page, per_page)
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

        val requestCall = buildApi(buildClient(), CollectionApi::class.java)
            .getCuratedCollections(BuildConfig.WALLBOX_ACCESS_KEY, page, per_page)
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

    /*fun requestCollection(id: String, listener: OnRequestCollectionListener?) {
        Log.d(TAG, "requestCollection: called >>>>>>>>>> Services")

        val request = buildApi(buildClient(), CollectionApi::class.java)
            .getCollection(id, BuildConfig.WALLBOX_ACCESS_KEY)
        request.enqueue(object : Callback<Collection> {
            override fun onResponse(call: Call<Collection>, response: Response<Collection>) {
                listener?.onRequestCollectionSuccess(call, response)
            }

            override fun onFailure(call: Call<Collection>, t: Throwable) {
                listener?.onRequestCollectionFailed(call, t)
            }
        })
    }*/

    fun requestUserCollections(user_name: String, page: Int, per_page: Int, listener: OnRequestCollectionsListener?) {
        Log.d(TAG, "requestUserCollections: called >>>>>>>>>> Services")

        val request = buildApi(buildClient(), CollectionApi::class.java)
            .getUserCollections(user_name, BuildConfig.WALLBOX_ACCESS_KEY, page, per_page)
        request.enqueue(object : Callback<List<Collection>> {
            override fun onResponse(call: Call<List<Collection>>, response: Response<List<Collection>>) {
                listener?.onRequestCollectionsSuccess(call, response)
            }

            override fun onFailure(call: Call<List<Collection>>, t: Throwable) {
                listener?.onRequestCollectionsFailed(call, t)
            }
        })
    }

    /** User services -------------------------------------------------------------------------------------------------*/
    fun requestUserProfile(username: String, listener: OnRequestUserProfileListener?) {
        Log.d(TAG, "requestUserProfile: called >>>>>>>>>> Services")

        val request = buildApi(buildClient(), UserApi::class.java).getUserProfile(username, BuildConfig.WALLBOX_ACCESS_KEY)
        request.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                listener?.onRequestUserProfileSuccess(call, response)
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                listener?.onRequestUserProfileFailed(call, t)
            }
        })
    }

    /** Search services -------------------------------------------------------------------------------------------------*/
    fun searchPhotos(query: String, page: Int, per_page: Int, collections: String?, listener: OnSearchPhotosListener?) {
        Log.d(TAG, "searchPhotos: called >>>>>>>>>> Services")

        val request = buildApi(buildClient(),SearchApi::class.java)
            .searchPhotos(BuildConfig.WALLBOX_ACCESS_KEY, query, page, per_page, collections)
        request.enqueue(object : Callback<SearchPhotos> {
            override fun onResponse(call: Call<SearchPhotos>, response: Response<SearchPhotos>) {
                listener?.onSearchPhotoSuccess(call, response)
            }

            override fun onFailure(call: Call<SearchPhotos>, t: Throwable) {
                listener?.onSearchPhotoFailed(call, t)
            }
        })
    }

    fun searchCollections(query: String, page: Int, per_page: Int, listener: OnSearchCollectionsListener?) {
        Log.d(TAG, "searchCollections: called >>>>>>>>>> Services")

        val request = buildApi(buildClient(), SearchApi::class.java)
            .searchCollections(BuildConfig.WALLBOX_ACCESS_KEY, query, page, per_page)
        request.enqueue(object : Callback<SearchCollections> {
            override fun onResponse(call: Call<SearchCollections>, response: Response<SearchCollections>) {
                listener?.onSearchCollectionsSuccess(call, response)
            }

            override fun onFailure(call: Call<SearchCollections>, t: Throwable) {
                listener?.onSearchCollectionsFailed(call, t)
            }
        })
    }

    fun searchUsers(query: String, page: Int, per_page: Int, listener: OnSearchUsersListener?) {
        Log.d(TAG, "searchUsers: called >>>>>>>>>> Services")

        val request = buildApi(buildClient(), SearchApi::class.java)
            .searchUsers(BuildConfig.WALLBOX_ACCESS_KEY, query, page, per_page)
        request.enqueue(object : Callback<SearchUsers> {
            override fun onResponse(call: Call<SearchUsers>, response: Response<SearchUsers>) {
                listener?.onSearchUsersSuccess(call, response)
            }

            override fun onFailure(call: Call<SearchUsers>, t: Throwable) {
                listener?.onSearchUsersFailed(call, t)
            }
        })
    }

    /** TODO: Authorization service */

    /*fun cancel() {
        if (photosCall != null) photosCall?.cancel()
        if (collectionsCall != null) collectionsCall?.cancel()
    }*/

    /** build. ------------------------------------------------------------------------------------------------------ */
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
    interface OnRequestCollectionListener {
        fun onRequestCollectionSuccess(call: Call<Collection>, response: Response<Collection>)
        fun onRequestCollectionFailed(call: Call<Collection>, t: Throwable)
    }

    // User listener
    interface OnRequestUserProfileListener {
        fun onRequestUserProfileSuccess(call: Call<User>, response: Response<User>)
        fun onRequestUserProfileFailed(call: Call<User>, t: Throwable)
    }

    // Search listener
    interface OnSearchPhotosListener {
        fun onSearchPhotoSuccess(call: Call<SearchPhotos>, response: Response<SearchPhotos>)
        fun onSearchPhotoFailed(call: Call<SearchPhotos>, t: Throwable)
    }
    interface OnSearchCollectionsListener {
        fun onSearchCollectionsSuccess(call: Call<SearchCollections>, response: Response<SearchCollections>)
        fun onSearchCollectionsFailed(call: Call<SearchCollections>, t: Throwable)
    }
    interface OnSearchUsersListener {
        fun onSearchUsersSuccess(call: Call<SearchUsers>, response: Response<SearchUsers>)
        fun onSearchUsersFailed(call: Call<SearchUsers>, t: Throwable)
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