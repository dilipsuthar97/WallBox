package com.dilipsuthar.wallbox.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.WallBox
import com.dilipsuthar.wallbox.data_source.managers.AuthManager
import com.dilipsuthar.wallbox.data_source.model.AccessToken
import com.dilipsuthar.wallbox.data_source.service.AuthorizationService
import com.dilipsuthar.wallbox.helpers.openWebView
import com.dilipsuthar.wallbox.utils.PopupUtils
import com.dilipsuthar.wallbox.utils.ThemeUtils
import com.dilipsuthar.wallbox.utils.Tools
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Response

class LoginActivity : BaseActivity(), AuthorizationService.OnRequestAccessTokenListener {
    private val TAG = LoginActivity::class.java.simpleName

    // View
    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.btn_login) lateinit var btnLogin: MaterialButton
    @BindView(R.id.btn_join) lateinit var btnJoin: MaterialButton
    @BindView(R.id.activity_login) lateinit var rootView: View

    // vars
    private var mService: AuthorizationService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)

        mService = AuthorizationService.getService()

        initToolbar()
        initComponent()
    }

    /** @mthod init toolbar settings */
    private fun initToolbar() {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.title = resources.getString(R.string.toolbar_title_login)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.changeNavigationIconColor(toolbar, ThemeUtils.getThemeAttrColor(this, R.attr.colorAccent))
    }

    /** @method init all components */
    private fun initComponent() {
        btnLogin.setOnClickListener {
            val url = WallBox.getLoginUrl(this)
            openWebView(url)
        }

        btnJoin.setOnClickListener {
            val url = WallBox.UNSPLASH_JOIN_URL
            openWebView(url)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d(TAG, "onNewIntent: called")
        if (intent != null
            && intent.data != null
            && intent.data?.authority != null
            && intent.data?.authority.equals(WallBox.UNSPLASH_LOGIN_CALLBACK) ) {
            val code = intent.data?.getQueryParameter("code")

            mService?.requestAccessToken(code!!, this)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) onBackPressed()

        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        mService?.cancel()
    }

    override fun onRequestAccessTokenSuccess(call: Call<AccessToken>, response: Response<AccessToken>) {
        if (response.isSuccessful) {
            Log.i(TAG, response.body().toString())

            AuthManager.getInstance().saveAccessToken(response.body())
            AuthManager.getInstance().requestUserProfileData()
            onBackPressed()
        } else {
            Log.d(TAG, response.message() + " " + response.errorBody() + " " + response.code())
            PopupUtils.showSnackbar(rootView, "Request token failed")
        }
    }

    override fun onRequestAccessTokenFailed(call: Call<AccessToken>, t: Throwable) {
        Log.e(TAG, t.message!!)
    }
}