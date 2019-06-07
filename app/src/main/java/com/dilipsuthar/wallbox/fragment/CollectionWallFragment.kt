package com.dilipsuthar.wallbox.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.dilipsuthar.wallbox.R

/**
 * A simple [Fragment] subclass.
 *
 */

class CollectionWallFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collection_wall, container, false)
    }


}
