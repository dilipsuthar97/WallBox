package com.dilipsuthar.wallbox.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife

import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.utils.VerticalSpacingItemDecorator

/**
 * A simple [Fragment] subclass.
 *
 */
class RecentWallFragment : Fragment() {

    companion object {
        private const val TAG = "RecentWallpaperListFragment"
    }

    @BindView(R.id.recent_wallpaper_list) lateinit var mRecyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_recent_wall, container, false)
        ButterKnife.bind(view)

        initComponent(view)
        return view
    }

    private fun initComponent(itemView: View) {

    }

    private fun subscribeObservers() {

    }

    private fun initRecyclerView() {
        val itemDecorator = VerticalSpacingItemDecorator(5)
        mRecyclerView.addItemDecoration(itemDecorator)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.setHasFixedSize(true)
    }

}
