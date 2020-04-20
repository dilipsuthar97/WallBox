package com.dilipsuthar.wallbox.data_source.service

import android.util.Log
import com.dilipsuthar.wallbox.WallBox
import com.dilipsuthar.wallbox.data_source.api.AuthorizationApi
import com.dilipsuthar.wallbox.data_source.model.AccessToken
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Dilip Suthar on 18/04/20
 * */

class AuthorizationService {
    private val TAG = AuthorizationService::class.java.simpleName

    private var call: Call<AccessToken>? = null

    /** Authorization services ---------------------------------------------------------------------------------------------- */
    fun requestAccessToken(code: String, listener: OnRequestAccessTokenListener?) {
        Log.d(TAG, "requestAccessToken: called >>>>>>>>>> AuthorizationService")
        val request = buildApi(buildClient()).getAccessToken(
            WallBox.getAccessKey(),
            WallBox.getSecretKey(),
            "wallbox://" + WallBox.UNSPLASH_LOGIN_CALLBACK,
            code,
            "authorization_code")

        request.enqueue(object : Callback<AccessToken> {
            override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                listener?.onRequestAccessTokenSuccess(call, response)
            }

            override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                listener?.onRequestAccessTokenFailed(call, t)
            }
        })
        call = request
    }

    fun cancel() {
        if (call != null) {
            call?.cancel()
        }
    }

    /** build. ------------------------------------------------------------------------------------------------------ */
    // Create/build okHttp client and return it ---->>>
    private fun buildClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .build()
    }

    // Create/build Retrofit API client and return it ---->>>
    private fun buildApi(okHttpClient: OkHttpClient): AuthorizationApi {
        return Retrofit.Builder()
            .baseUrl(WallBox.UNSPLASH_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthorizationApi::class.java)
    }

    /** interface */
    interface OnRequestAccessTokenListener {
        fun onRequestAccessTokenSuccess(call: Call<AccessToken>, response: Response<AccessToken>)
        fun onRequestAccessTokenFailed(call: Call<AccessToken>, t: Throwable)
    }

    /** Singleton pattern */
    companion object {
        private var instance: AuthorizationService? = null

        fun getService(): AuthorizationService? {
            if (instance == null) {
                synchronized(AuthorizationService::class.java) {
                    if (instance == null) {
                        instance = AuthorizationService()
                    }
                }
            }
            return instance
        }
    }

}