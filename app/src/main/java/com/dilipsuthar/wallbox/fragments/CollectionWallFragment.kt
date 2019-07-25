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
 *
 */

class CollectionWallFragment : Fragment() {

    companion object {
        fun newInstance(sort: String): CollectionWallFragment {
            val fragment = CollectionWallFragment()

            val args = Bundle()
            args.putString(Preferences.SORT, sort)
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true
        return inflater.inflate(R.layout.fragment_collection_wall, container, false)
    }


}
