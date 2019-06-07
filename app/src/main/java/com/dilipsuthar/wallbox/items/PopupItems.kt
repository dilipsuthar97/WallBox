package com.dilipsuthar.wallbox.items

import android.content.Context
import androidx.annotation.DrawableRes
import com.dilipsuthar.wallbox.R

class PopupItem constructor(title: String) {

    private var mTitle: String = title
    private var mIcon: Int = 0
    private var mIsSelected:Boolean = false
    private lateinit var mType: Type

    public fun setIcon(@DrawableRes icon: Int): PopupItem {
        mIcon = icon
        return this
    }

    public fun setType(type: Type): PopupItem {
        mType = type
        return this
    }

    public enum class Type {
        SORT_LATEST,
        SORT_OLDEST,
        SORT_POPULAR
    }

    companion object {

        public fun getSortItems(context: Context, selection: Boolean): ArrayList<PopupItem> {
            val items: ArrayList<PopupItem> = ArrayList()
            items.add(PopupItem(context.resources.getString(R.string.menu_sort_latest))
                .setType(Type.SORT_LATEST)
                .setIcon(R.drawable.ic_toolbar_sort_latest))

            items.add(PopupItem(context.resources.getString(R.string.menu_sort_oldest))
                .setType(Type.SORT_OLDEST)
                .setIcon(R.drawable.ic_toolbar_sort_oldest))

            items.add(PopupItem(context.resources.getString(R.string.menu_sort_popular))
                .setType(Type.SORT_POPULAR)
                .setIcon(R.drawable.ic_toolbar_sort_popular))

            return items
        }

    }

}