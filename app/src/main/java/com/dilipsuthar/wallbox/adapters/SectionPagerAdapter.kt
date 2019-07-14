package com.dilipsuthar.wallbox.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class SectionPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {

    private var fragmentList: ArrayList<Fragment> = ArrayList<Fragment>()
    private var fragmentTitleList: ArrayList<String> = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitleList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }

}