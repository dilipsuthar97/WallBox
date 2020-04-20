package com.dilipsuthar.wallbox.data_source.managers

import android.text.TextUtils
import android.util.Log
import com.dilipsuthar.wallbox.WallBox
import com.dilipsuthar.wallbox.data_source.model.AccessToken
import com.dilipsuthar.wallbox.data_source.model.Me
import com.dilipsuthar.wallbox.data_source.model.User
import com.dilipsuthar.wallbox.data_source.service.Services
import com.dilipsuthar.wallbox.preferences.Prefs
import com.dilipsuthar.wallbox.preferences.SharedPref
import retrofit2.Call
import retrofit2.Response

class AuthManager : Services.OnRequestMeProfileListener, Services.OnRequestUserProfileListener {
    private var TAG = AuthManager::class.java.simpleName

    private val mSharedPref = SharedPref.getInstance(WallBox.getInstance().getContext())

    private var me: Me? = null
    private var user: User? = null
    private lateinit var mService: Services

    private lateinit var _accessToken: String
    private lateinit var _id: String
    private lateinit var _username: String
    private lateinit var _firstName: String
    private lateinit var _lastName: String
    private lateinit var _email: String
    private lateinit var _profileUrl: String
    private var _authorized: Boolean = false

    private var mListener: OnAuthStateChangeListener? = null

    companion object {
        private var instance: AuthManager? = null

        // singleton pattern
        fun getInstance(): AuthManager {
            if (instance == null) {
                synchronized(AuthManager::class.java) {
                    if (instance == null) {
                        instance = AuthManager()
                    }
                }
            }
            return instance as AuthManager
        }
    }

    // constructor
    init {
        _accessToken = mSharedPref.getString(Prefs.AUTH_TOKEN, "")!!
        _authorized = !TextUtils.isEmpty(_accessToken)
        mListener = null

        if (_authorized) {
            _id = mSharedPref.getString(Prefs.AUTH_ID, "")!!
            _username = mSharedPref.getString(Prefs.AUTH_USERNAME, "")!!
            _firstName = mSharedPref.getString(Prefs.AUTH_FIRST_NAME, "")!!
            _lastName = mSharedPref.getString(Prefs.AUTH_LAST_NAME, "")!!
            _email= mSharedPref.getString(Prefs.AUTH_EMAIL, "")!!
            _profileUrl= mSharedPref.getString(Prefs.AUTH_PROFILE_URL, "")!!
        }

        me = null
        user = null
        mService = Services.getService()
    }

    // methods
    fun requestUserProfileData() {
        if(isAuthorized()) {
            mService.cancel(Services.CallType.USER)
            mService.requestMeProfile(this)
        }
    }

    fun cancelRequests() {
        mService.cancel(Services.CallType.USER)
    }

    // getter
    fun getMe(): Me? = me
    fun getUser(): User? = user
    fun getAccessToken(): String = _accessToken
    fun getId(): String = _id
    fun getUsername(): String = _username
    fun getFirstName(): String = _firstName
    fun getLastName(): String = _lastName
    fun getEmail(): String = _email
    fun getProfileUrl(): String = _profileUrl
    fun isAuthorized(): Boolean = _authorized

    // setter
    fun saveAccessToken(accessToken: AccessToken?) {
        mSharedPref.saveData(Prefs.AUTH_TOKEN, accessToken?.access_token!!)
        _accessToken = accessToken.access_token
        _authorized = true

        Log.d(TAG, _accessToken)
        mListener?.onSaveAccessToken()
    }


    private fun saveUserInfo(me: Me?) {
        me?.let {
            mSharedPref.saveData(Prefs.AUTH_ID, me.id)
            mSharedPref.saveData(Prefs.AUTH_USERNAME, me.username)
            mSharedPref.saveData(Prefs.AUTH_FIRST_NAME, me.first_name)
            mSharedPref.saveData(Prefs.AUTH_LAST_NAME, me.last_name)
            mSharedPref.saveData(Prefs.AUTH_EMAIL, me.email)

            this.me = me
            _id = me.id
            _username = me.username
            _firstName = me.first_name
            _lastName = me.last_name
            _email = me.email

            mListener?.onLogin()
        }
    }

    private fun saveUserInfo(user: User?) {
        user?.let {
            mSharedPref.saveData(Prefs.AUTH_ID, user.id)
            mSharedPref.saveData(Prefs.AUTH_USERNAME, user.username)
            mSharedPref.saveData(Prefs.AUTH_FIRST_NAME, user.first_name)
            mSharedPref.saveData(Prefs.AUTH_LAST_NAME, user.last_name)
            mSharedPref.saveData(Prefs.AUTH_PROFILE_URL, user.profile_image.large)

            this.user = user
            _profileUrl = user.profile_image.large

            mListener?.onSaveProfileUrl()
        }
    }

    fun logout() {
        mService.cancel(Services.CallType.USER)

        mListener?.onLogout()
    }

    // listeners
    // on request me profile
    override fun onRequestMeProfileSuccess(call: Call<Me>, response: Response<Me>) {
        if (response.isSuccessful && response.body() != null && isAuthorized()) {
            saveUserInfo(response.body())
            mService.requestUserProfile(response.body()?.username!!, this)
        } else if(isAuthorized()) {
            mService.requestMeProfile(this)
        }
    }

    override fun onRequestMeProfileFailed(call: Call<Me>, t: Throwable) {
        if (isAuthorized()) {
            mService.requestMeProfile(this)
        }
    }

    // on request user profile
    override fun onRequestUserProfileSuccess(call: Call<User>, response: Response<User>) {
        if (response.isSuccessful && response.body() != null && isAuthorized()) {
            saveUserInfo(response.body())
        } else if (isAuthorized()) {
            mService.requestUserProfile(getUsername(), this)
        }
    }

    override fun onRequestUserProfileFailed(call: Call<User>, t: Throwable) {
        if (isAuthorized()) {
            mService.requestUserProfile(getUsername(), this)
        }
    }

    // interface
    interface OnAuthStateChangeListener {
        fun onSaveAccessToken()
        fun onLogin()
        fun onSaveProfileUrl()
        fun onLogout()
    }

    fun addOnAuthStateChangeListener(l: OnAuthStateChangeListener) {
        mListener = l
    }

    fun removeOnAuthStateChangeListener() {
        if (mListener != null) {
            mListener = null
        }
    }

}