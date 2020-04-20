package com.dilipsuthar.wallbox.data_source.service

import android.os.AsyncTask
import android.util.Log
import com.dilipsuthar.wallbox.WallBox
import com.dilipsuthar.wallbox.data_source.api.CollectionApi
import com.dilipsuthar.wallbox.data_source.api.PhotoApi
import com.dilipsuthar.wallbox.data_source.api.SearchApi
import com.dilipsuthar.wallbox.data_source.api.UserApi
import com.dilipsuthar.wallbox.data_source.managers.AuthManager
import com.dilipsuthar.wallbox.data_source.model.*
import com.dilipsuthar.wallbox.data_source.model.Collection
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Dilip Suthar on 20/07/19
 * */

class Services {
    private val TAG = Services::class.java.simpleName

    enum class CallType {
        PHOTO, COLLECTION, USER
    }

    private val call: Call<*>? = null
    private var photoCall: Call<*>? = null
    private var collectionCall: Call<*>? = null
    private var userCall: Call<*>? = null

    /** Photo services ---------------------------------------------------------------------------------------------- */
    fun requestPhotos(page: Int, per_page: Int, order_by: String, listener: OnRequestPhotosListener?) {
        val requestCall = buildApi(buildClient(), PhotoApi::class.java).getPhotos(page, per_page, order_by)

        requestCall.enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                listener?.onRequestPhotosSuccess(call, response)
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                listener?.onRequestPhotosFailed(call, t)
            }
        })
        photoCall = requestCall
    }

    fun requestCuratedPhotos(page: Int, per_page: Int, order_by: String, listener: OnRequestPhotosListener?) {
        val requestCall = buildApi(buildClient(), PhotoApi::class.java).getCuratedPhotos(page, per_page, order_by)

        requestCall.enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                listener?.onRequestPhotosSuccess(call, response)
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                listener?.onRequestPhotosFailed(call, t)
            }
        })
        photoCall = requestCall
    }

    fun requestPhoto(id: String, listener: OnRequestPhotoListener?) {
        val request = buildApi(buildClient(), PhotoApi::class.java).getPhoto(id)

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
        val request = buildApi(buildClient(), PhotoApi::class.java).getPhotoStatistics(id)

        request.enqueue(object : Callback<PhotoStatistics> {
            override fun onResponse(call: Call<PhotoStatistics>, response: Response<PhotoStatistics>) {
                listener?.onRequestSuccess(call, response)
            }

            override fun onFailure(call: Call<PhotoStatistics>, t: Throwable) {
                listener?.onRequestFailed(call, t)
            }
        })
        photoCall = request
    }

    fun requestCollectionPhotos(id: String, page: Int, per_page: Int, listener: OnRequestPhotosListener?) {
        val request = buildApi(buildClient(), PhotoApi::class.java)
            .getCollectionPhotos(id, page, per_page)

        request.enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                listener?.onRequestPhotosSuccess(call, response)
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                listener?.onRequestPhotosFailed(call, t)
            }
        })
        collectionCall = request
    }

    fun requestCuratedCollectionPhotos(id: String, page: Int, per_page: Int, listener: OnRequestPhotosListener?) {
        val request = buildApi(buildClient(), PhotoApi::class.java)
            .getCuratedCollectionPhotos(id, page, per_page)

        request.enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                listener?.onRequestPhotosSuccess(call, response)
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                listener?.onRequestPhotosFailed(call, t)
            }
        })
        collectionCall = request
    }

    fun requestUserPhotos(user_name: String, page: Int, per_page: Int, order_by: String, listener: OnRequestPhotosListener?) {
        val request = buildApi(buildClient(), PhotoApi::class.java)
            .getUserPhotos(user_name, page, per_page, order_by)

        request.enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                listener?.onRequestPhotosSuccess(call, response)
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                listener?.onRequestPhotosFailed(call, t)
            }
        })
        photoCall = request
    }

    fun requestUserLikedPhotos(user_name: String, page: Int, per_page: Int, order_by: String, listener: OnRequestPhotosListener?) {
        val request = buildApi(buildClient(), PhotoApi::class.java)
            .getUserLikedPhotos(user_name, page, per_page, order_by)

        request.enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                listener?.onRequestPhotosSuccess(call, response)
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                listener?.onRequestPhotosFailed(call, t)
            }
        })
        photoCall = request
    }


    /** Collection services ----------------------------------------------------------------------------------------- */
    fun requestCollections(page: Int, per_page: Int, listener: OnRequestCollectionsListener?) {
        Log.d(TAG, "requestCollections: called >>>>>>>>>> Services")

        val requestCall = buildApi(buildClient(), CollectionApi::class.java)
            .getCollections(page, per_page)
        requestCall.enqueue(object : Callback<List<Collection>> {
            override fun onResponse(call: Call<List<Collection>>, response: Response<List<Collection>>) {
                listener?.onRequestCollectionsSuccess(call, response)
            }

            override fun onFailure(call: Call<List<Collection>>, t: Throwable) {
                listener?.onRequestCollectionsFailed(call, t)
            }
        })
        collectionCall = requestCall
    }

    fun requestFeaturedCollections(page: Int, per_page: Int, listener: OnRequestCollectionsListener?) {
        Log.d(TAG, "requestFeaturedCollections: called >>>>>>>>>> Services")

        val requestCall = buildApi(buildClient(), CollectionApi::class.java)
            .getFeaturedCollections(page, per_page)
        requestCall.enqueue(object : Callback<List<Collection>> {
            override fun onResponse(call: Call<List<Collection>>, response: Response<List<Collection>>) {
                listener?.onRequestCollectionsSuccess(call, response)
            }

            override fun onFailure(call: Call<List<Collection>>, t: Throwable) {
                listener?.onRequestCollectionsFailed(call, t)
            }
        })
        collectionCall = requestCall
    }

    fun requestCuratedCollections(page: Int, per_page: Int, listener: OnRequestCollectionsListener?) {
        Log.d(TAG, "requestCuratedCollections: called >>>>>>>>>> Services")

        val requestCall = buildApi(buildClient(), CollectionApi::class.java)
            .getCuratedCollections(page, per_page)
        requestCall.enqueue(object : Callback<List<Collection>> {
            override fun onResponse(call: Call<List<Collection>>, response: Response<List<Collection>>) {
                listener?.onRequestCollectionsSuccess(call, response)
            }

            override fun onFailure(call: Call<List<Collection>>, t: Throwable) {
                listener?.onRequestCollectionsFailed(call, t)
            }
        })
        collectionCall = requestCall
    }

    /*fun requestCollection(id: String, listener: OnRequestCollectionListener?) {
        Log.d(TAG, "requestCollection: called >>>>>>>>>> Services")

        val request = buildApi(buildClient(), CollectionApi::class.java)
            .getCollection(id)
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
            .getUserCollections(user_name, page, per_page)
        request.enqueue(object : Callback<List<Collection>> {
            override fun onResponse(call: Call<List<Collection>>, response: Response<List<Collection>>) {
                listener?.onRequestCollectionsSuccess(call, response)
            }

            override fun onFailure(call: Call<List<Collection>>, t: Throwable) {
                listener?.onRequestCollectionsFailed(call, t)
            }
        })
        collectionCall = request
    }

    /** User services -------------------------------------------------------------------------------------------------*/
    fun requestUserProfile(username: String, listener: OnRequestUserProfileListener?) {
        Log.d(TAG, "requestUserProfile: called >>>>>>>>>> Services")

        val request = buildApi(buildClient(), UserApi::class.java).getUserProfile(username)
        request.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                listener?.onRequestUserProfileSuccess(call, response)
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                listener?.onRequestUserProfileFailed(call, t)
            }
        })
        userCall = request
    }

    fun requestMeProfile(listener: OnRequestMeProfileListener?) {
        Log.d(TAG, "requestMeProfile: called >>>>>>>>>> Services")

        val request = buildApi(buildClient(), UserApi::class.java).getMeProfile()
        request.enqueue(object : Callback<Me> {
            override fun onResponse(call: Call<Me>, response: Response<Me>) {
                listener?.onRequestMeProfileSuccess(call, response)
            }

            override fun onFailure(call: Call<Me>, t: Throwable) {
                listener?.onRequestMeProfileFailed(call, t)
            }
        })
        userCall = request
    }


    /** Search services -------------------------------------------------------------------------------------------------*/
    fun searchPhotos(query: String, page: Int, per_page: Int, collections: String?, listener: OnSearchPhotosListener?) {
        Log.d(TAG, "searchPhotos: called >>>>>>>>>> Services")

        val request = buildApi(buildClient(),SearchApi::class.java)
            .searchPhotos(query, page, per_page, collections)
        request.enqueue(object : Callback<SearchPhotos> {
            override fun onResponse(call: Call<SearchPhotos>, response: Response<SearchPhotos>) {
                listener?.onSearchPhotoSuccess(call, response)
            }

            override fun onFailure(call: Call<SearchPhotos>, t: Throwable) {
                listener?.onSearchPhotoFailed(call, t)
            }
        })
        photoCall = request
    }

    fun searchCollections(query: String, page: Int, per_page: Int, listener: OnSearchCollectionsListener?) {
        Log.d(TAG, "searchCollections: called >>>>>>>>>> Services")

        val request = buildApi(buildClient(), SearchApi::class.java)
            .searchCollections(query, page, per_page)
        request.enqueue(object : Callback<SearchCollections> {
            override fun onResponse(call: Call<SearchCollections>, response: Response<SearchCollections>) {
                listener?.onSearchCollectionsSuccess(call, response)
            }

            override fun onFailure(call: Call<SearchCollections>, t: Throwable) {
                listener?.onSearchCollectionsFailed(call, t)
            }
        })
        collectionCall = request
    }

    fun searchUsers(query: String, page: Int, per_page: Int, listener: OnSearchUsersListener?) {
        Log.d(TAG, "searchUsers: called >>>>>>>>>> Services")

        val request = buildApi(buildClient(), SearchApi::class.java)
            .searchUsers(query, page, per_page)
        request.enqueue(object : Callback<SearchUsers> {
            override fun onResponse(call: Call<SearchUsers>, response: Response<SearchUsers>) {
                listener?.onSearchUsersSuccess(call, response)
            }

            override fun onFailure(call: Call<SearchUsers>, t: Throwable) {
                listener?.onSearchUsersFailed(call, t)
            }
        })
        userCall = request
    }

    fun cancel(callType: CallType) {
        when (callType) {
            CallType.USER -> userCall?.cancel()
            CallType.COLLECTION -> collectionCall?.cancel()
            CallType.PHOTO -> photoCall?.cancel()
        }
    }

    fun cancelAll() {
        photoCall?.cancel()
        collectionCall?.cancel()
        userCall?.cancel()
    }

    /** build. ------------------------------------------------------------------------------------------------------ */
    // Create/build okHttp client and return it ---->>>
    private fun buildClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor {chain ->
                // Header configuration ...
                val newRequest: Request
                if (AuthManager.getInstance().isAuthorized()) {
                    Log.d(TAG, "buildClient: using Bearer ${AuthManager.getInstance().getAccessToken()}")
                    newRequest = chain
                        .request()
                        .newBuilder()
                        .addHeader("Authorization", "Bearer ${AuthManager.getInstance().getAccessToken()}")
                        .build()
                } else {
                    Log.d(TAG, "buildClient: using Client-ID ${WallBox.getAccessKey()}")
                    newRequest = chain
                        .request()
                        .newBuilder()
                        .addHeader("Authorization", "Client-ID ${WallBox.getAccessKey()}")
                        .build()
                }

                chain.proceed(newRequest)
            }
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

    interface OnRequestMeProfileListener {
        fun onRequestMeProfileSuccess(call: Call<Me>, response: Response<Me>)
        fun onRequestMeProfileFailed(call: Call<Me>, t: Throwable)
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
        private var instance: Services? = null

        fun getService(): Services {
            if (instance == null)
                instance =
                    Services()

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