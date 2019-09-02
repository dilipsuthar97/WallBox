package com.dilipsuthar.wallbox.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.adapters.SectionPagerAdapter
import com.dilipsuthar.wallbox.fragments.SearchCollectionFragment
import com.dilipsuthar.wallbox.fragments.SearchPhotoFragment
import com.dilipsuthar.wallbox.fragments.SearchUserFragment
import com.dilipsuthar.wallbox.utils.PopupUtils
import com.google.android.material.tabs.TabLayout
import java.util.*

class SearchActivity : BaseActivity() {

    @BindView(R.id.search_tab_layout) lateinit var mTabLayout: TabLayout
    @BindView(R.id.search_view_pager) lateinit var mViewPager: ViewPager
    @BindView(R.id.et_search) lateinit var etSearch: EditText
    @BindView(R.id.btn_close) lateinit var btnClose: ImageButton
    @BindView(R.id.btn_speech_to_txt) lateinit var btnSpeechToTxt: ImageButton

    private var mViewPagerAdapter: SectionPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        ButterKnife.bind(this)

        /** Set mViewPager and link it with TabLayout */
        mViewPagerAdapter = SectionPagerAdapter(supportFragmentManager)
        with(mViewPagerAdapter!!) {
            addFragment(SearchPhotoFragment.newInstance(""), resources.getString(R.string.tab_title_search_photo_fragment))
            addFragment(SearchCollectionFragment.newInstance(""), resources.getString(R.string.tab_title_search_collection_fragment))
            addFragment(SearchUserFragment.newInstance(""), resources.getString(R.string.tab_title_search_user_fragment))
            mViewPager.adapter = this
        }
        mViewPager.offscreenPageLimit = 2
        mTabLayout.setupWithViewPager(mViewPager)

        /** Search edit text */
        etSearch.requestFocus()
        etSearch.setOnEditorActionListener { textView, i, keyEvent ->

            searchQuery()
            true
        }

        /** Listeners */
        btnClose.setOnClickListener {
            onBackPressed()
        }

        // Button google Speech-to-text listener
        btnSpeechToTxt.setOnClickListener {
            /** This will open google's speech-to-text activity */
            val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH)
            speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

            if (speechIntent.resolveActivity(packageManager) != null)
                startActivityForResult(speechIntent, 101)
            else PopupUtils.showToast(this, resources.getString(R.string.msg_no_speech_to_text_support), Toast.LENGTH_SHORT)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            101 -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    etSearch.setText(result?.get(0) ?: "")
                    searchQuery()
                }
            }
        }
    }

    /** @method search query and change all search fragments */
    private fun searchQuery() {
        val query = etSearch.text.toString()
        if (query.isNotEmpty()) {
            Log.d(HomeActivity.TAG, query)
            supportFragmentManager.beginTransaction().replace(R.id.search_photo_fragment, SearchPhotoFragment.newInstance(query))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()

            supportFragmentManager.beginTransaction().replace(R.id.search_collection_fragment, SearchCollectionFragment.newInstance(query))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()

            supportFragmentManager.beginTransaction().replace(R.id.search_user_fragment, SearchUserFragment.newInstance(query))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
        }

        val view = this.currentFocus
        if (view != null) {
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
