package com.dilipsuthar.wallbox.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.preferences.Preferences

/**
 * A simple [Fragment] subclass.
 */
class SearchUserFragment : Fragment() {
    private val TAG = "WallBox.Search"

    companion object {
        fun newInstance(query: String): SearchUserFragment {
            val fragment = SearchUserFragment()

            val args = Bundle()
            args.putString(Preferences.SEARCH_QUERY, query)
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true
        return inflater.inflate(R.layout.fragment_search_user, container, false)
    }


}
