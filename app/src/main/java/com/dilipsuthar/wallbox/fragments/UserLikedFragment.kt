package com.dilipsuthar.wallbox.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife

import com.dilipsuthar.wallbox.R

/**
 * A simple [Fragment] subclass.
 */
class UserLikedFragment : Fragment() {
    private val TAG = "WallBox.UserLP_Frag"

    companion object {
        fun newInstance(): UserLikedFragment {
            return UserLikedFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_user_liked, container, false)
        ButterKnife.bind(this, view)

        return view
    }


}
