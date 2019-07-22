package com.dilipsuthar.wallbox.data.service

import com.dilipsuthar.wallbox.WallBox
import com.dilipsuthar.wallbox.data.api.PhotoApi
import com.dilipsuthar.wallbox.data.model.Photo
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** Created by Dilip on 20/07/19 */

public class PhotoService {
    private var call: Call<List<Photo>>? = null

    public fun requestPhotos(page: Int, per_page: Int, order_by: String, listener: OnRequestPhotosListener?) {
        val requestCall = buildApi(buildClient()).getPhotos(WallBox.ACCESS_KEY, page, per_page, order_by)
        requestCall.enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                listener?.onRequestPhotosSuccess(call, response)
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                listener?.onRequestPhotosFailed(call, t)
            }
        })
        call = requestCall
    }

    public fun cancel() {
        if (call != null) {
            call?.cancel()
        }
    }

    /** build. */

    // Create/build okHttp client and return it ---->>>
    private fun buildClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .build()
    }

    // Create/build Retrofit API client and return it ---->>>
    private fun buildApi(okHttpClient: OkHttpClient): PhotoApi {
        return Retrofit.Builder()
            .baseUrl(WallBox.UNSPLASH_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PhotoApi::class.java)
    }

    private fun buildTestApi(okHttpClient: OkHttpClient): PhotoApi {
        return Retrofit.Builder()
            .baseUrl("https://api.myjson.com/bins/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PhotoApi::class.java)
    }

    /** interface */
    interface OnRequestPhotosListener {
        fun onRequestPhotosSuccess(call: Call<List<Photo>>, response: Response<List<Photo>>)
        fun onRequestPhotosFailed(call: Call<List<Photo>>, t: Throwable)
    }

    /** Singleton pattern */
    companion object {
        private var instance: PhotoService? = null

        fun getService(): PhotoService {
            if (instance == null)
                instance = PhotoService()

            return instance as PhotoService
        }
    }

}