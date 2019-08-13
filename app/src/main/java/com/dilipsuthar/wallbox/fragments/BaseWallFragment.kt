package com.dilipsuthar.wallbox.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseWallFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = getInflateView(inflater, container, savedInstanceState)

        return view
    }

    abstract fun getInflateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View

}