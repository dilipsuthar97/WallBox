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
import com.afollestad.materialdialogs.list.getRecyclerView
import com.afollestad.materialdialogs.list.listItems
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.danimahardhika.android.helpers.core.FileHelper
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.helpers.LocaleHelper
import com.dilipsuthar.wallbox.items.Setting
import com.dilipsuthar.wallbox.preferences.Preferences
import com.dilipsuthar.wallbox.utils.PopupUtils
import com.dilipsuthar.wallbox.utils.ThemeUtils
import com.dilipsuthar.wallbox.utils.Tools
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat

/**
 * @adapter SettingActivity adapter to bind setting menu items
 *
 * @param settingList List of all setting menu
 * @param context Application context
 * @param activity Activity for recreate()
 */
class SettingAdapter(
    private val settingList: ArrayList<Setting>?,
    private val context: Context?,
    private val activity: Activity?
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val sharedPreferences = Preferences.getSharedPreferences(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_CONTENT) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_setting, parent, false)
            return ContentViewHolder(view)
        }

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_setting_footer, parent, false)
        return FooterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return settingList?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val setting = settingList?.get(position)
        if (holder is ContentViewHolder) {
            setting?.let {
                holder.tvTitle.text = it.title
                holder.tvSubtitle.text = it.subTitle

                if (it.content != "") {
                    Tools.visibleViews(holder.tvContent)
                    holder.tvContent.text = it.content
                } else Tools.inVisibleViews(holder.tvContent, type = Tools.GONE)

                if (it.checkState >= 0) {
                    Tools.visibleViews(holder.btnSwitch)
                    holder.btnSwitch.isChecked = it.checkState == 1
                } else Tools.inVisibleViews(holder.btnSwitch, type = Tools.GONE)

                /** Listeners */
                holder.btnSwitch.setOnCheckedChangeListener { _, isChecked ->
                    if (it.type == Setting.Type.THEME) {
                        if (isChecked) ThemeUtils.setTheme(context, ThemeUtils.DARK)
                        else ThemeUtils.setTheme(context, ThemeUtils.LIGHT)
                        activity?.recreate()
                        //ThemeUtils.restartApp(context)
                    }
                }

                /** Setting menu onClick listener */
                holder.btnSetting.setOnClickListener { _ ->
                    when (it.type) {
                        Setting.Type.LANGUAGE -> {

                            val languageCode = context!!.resources.getStringArray(R.array.languages_code)

                            MaterialDialog(context).show {
                                title(R.string.title_language)
                                cornerRadius(16f)
                                listItems(R.array.languages_name) { dialog, index, text ->
                                    sharedPreferences?.let { sharedPref ->
                                        LocaleHelper.loadLocal(context)
                                        sharedPref.edit().putString(Preferences.LANGUAGE, text).apply()
                                        sharedPref.edit().putString(Preferences.LANGUAGE_CODE, languageCode[index]).apply()
                                    }

                                    it.subTitle = text
                                    notifyItemChanged(position)
                                    activity?.recreate()
                                }
                            }
                        }
                        Setting.Type.THEME -> {
                            holder.btnSwitch.isChecked = !holder.btnSwitch.isChecked
                        }
                        Setting.Type.CLEAR_CACHE -> {
                            FileHelper.clearDirectory(context!!.cacheDir)
                            PopupUtils.showToast(context, context.resources?.getString(R.string.msg_clear_cache), Toast.LENGTH_SHORT)

                            val cacheSize = (FileHelper.getDirectorySize(context.cacheDir) / FileHelper.MB).toDouble()
                            val formatter = DecimalFormat("#0.00")

                            it.content = context.resources.getString(R.string.total_cache) + " ${formatter.format(cacheSize)} MB"

                            notifyItemChanged(position)

                        }
                        Setting.Type.WALLPAPER_PREVIEW_QUALITY -> {

                            MaterialDialog(context!!).show {
                                title(R.string.title_wallpaper_preview_quality)
                                cornerRadius(16f)
                                listItems(R.array.wallpaper_quality) { _, _, text ->
                                    sharedPreferences!!.edit().putString(Preferences.WALLPAPER_QUALITY, text).apply()
                                    setting.subTitle = text

                                    notifyItemChanged(position)
                                }
                            }

                        }
                        Setting.Type.RESET_TUTORIAL -> {
                            sharedPreferences!!.edit().putBoolean(Preferences.SHOW_INTRO_TUTORIAL, true).apply()
                            PopupUtils.showToast(context, context!!.resources.getString(R.string.msg_reset_tutorial), Toast.LENGTH_SHORT)
                        }
                        Setting.Type.UNSPLASH_SITE -> {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.subTitle))
                            context?.startActivity(intent)
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (settingList!![position].title != "") TYPE_CONTENT else TYPE_FOOTER
    }

    /** View holders */
    class ContentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        init {
            ButterKnife.bind(this, itemView)
        }

        @BindView(R.id.tv_title) lateinit var tvTitle: TextView
        @BindView(R.id.tv_subtitle) lateinit var tvSubtitle: TextView
        @BindView(R.id.tv_content) lateinit var tvContent: TextView
        @BindView(R.id.btn_switch) lateinit var btnSwitch: Switch
        @BindView(R.id.btn_setting) lateinit var btnSetting: LinearLayout
    }

    class FooterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    companion object{
        const val TYPE_CONTENT = 0
        const val TYPE_FOOTER = 1
    }

}