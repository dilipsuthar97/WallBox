package com.dilipsuthar.wallbox.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.danimahardhika.android.helpers.core.FileHelper
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.helpers.LocaleHelper
import com.dilipsuthar.wallbox.items.Setting
import com.dilipsuthar.wallbox.preferences.Preferences
import com.dilipsuthar.wallbox.utils.PopupUtils
import com.dilipsuthar.wallbox.utils.ThemeUtils
import com.dilipsuthar.wallbox.utils.Tools
import com.dilipsuthar.wallbox.viewholders.SettingsViewHolder
import java.text.DecimalFormat

/**
 * @adapter SettingActivity adapter to bind setting menu items
 *
 * @param settingList List of all setting menu
 * @param context Application context
 * @param activity Activity for recreate()
 */
class SettingsAdapter(
    private val settingList: ArrayList<Setting>?,
    private val context: Context,
    private val activity: Activity
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_CONTENT) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_setting, parent, false)
            return SettingsViewHolder.Content(view)
        }

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_setting_footer, parent, false)
        return SettingsViewHolder.Footer(view)
    }

    override fun getItemCount(): Int {
        return settingList?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val setting = settingList?.get(position)
        if (holder is SettingsViewHolder.Content) {
            holder.bind(setting, context, activity, this)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (settingList!![position].title != "") TYPE_CONTENT else TYPE_FOOTER
    }

    companion object{
        const val TYPE_CONTENT = 0
        const val TYPE_FOOTER = 1
    }

}