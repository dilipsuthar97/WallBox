package com.dilipsuthar.wallbox.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.adapters.SectionPagerAdapter
import com.dilipsuthar.wallbox.fragments.SearchCollectionFragment
import com.dilipsuthar.wallbox.fragments.SearchPhotoFragment
import com.dilipsuthar.wallbox.fragments.SearchUserFragment
import com.google.android.material.tabs.TabLayout

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
            addFragment(SearchPhotoFragment.newInstance(""), "Photo")
            addFragment(SearchCollectionFragment.newInstance(""), "Collection")
            addFragment(SearchUserFragment.newInstance(""), "User")
            mViewPager.adapter = this
        }
        mViewPager.offscreenPageLimit = 2
        mTabLayout.setupWithViewPager(mViewPager)

        /** Search edit text */
        etSearch.requestFocus()
        etSearch.setOnEditorActionListener { textView, i, keyEvent ->

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

            true
        }

        /** Listener */
        btnClose.setOnClickListener {
            onBackPressed()
        }

        btnSpeechToTxt.setOnClickListener {

        }

    }
}
