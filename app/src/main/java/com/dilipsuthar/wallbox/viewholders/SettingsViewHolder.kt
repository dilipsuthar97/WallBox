package com.dilipsuthar.wallbox.viewholders

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
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
import com.dilipsuthar.wallbox.adapters.SettingsAdapter
import com.dilipsuthar.wallbox.helpers.LocaleHelper
import com.dilipsuthar.wallbox.items.Setting
import com.dilipsuthar.wallbox.preferences.Preferences
import com.dilipsuthar.wallbox.utils.PopupUtils
import com.dilipsuthar.wallbox.utils.ThemeUtils
import com.dilipsuthar.wallbox.utils.Tools
import java.text.DecimalFormat

object SettingsViewHolder {

    class Content(itemView: View): RecyclerView.ViewHolder(itemView) {
        init {
            ButterKnife.bind(this, itemView)
        }

        @BindView(R.id.tv_title) lateinit var tvTitle: TextView
        @BindView(R.id.tv_subtitle) lateinit var tvSubtitle: TextView
        @BindView(R.id.tv_content) lateinit var tvContent: TextView
        @BindView(R.id.btn_switch) lateinit var btnSwitch: Switch
        @BindView(R.id.btn_setting) lateinit var btnSetting: LinearLayout

        fun bind(setting: Setting?, ctx: Context, act: Activity, adapter: SettingsAdapter) {
            val sharedPreferences = Preferences.getSharedPreferences(ctx)
            setting?.let {

                tvTitle.text = it.title
                tvSubtitle.text = it.subTitle

                if (it.content != "") {
                    Tools.visibleViews(tvContent)
                    tvContent.text = it.content
                } else Tools.inVisibleViews(tvContent, type = Tools.GONE)

                if (it.checkState >= 0) {
                    Tools.visibleViews(btnSwitch)
                    btnSwitch.isChecked = it.checkState == 1
                } else Tools.inVisibleViews(btnSwitch, type = Tools.GONE)

                /** Listeners */
                btnSwitch.setOnCheckedChangeListener { _, isChecked ->
                    if (it.type == Setting.Type.THEME) {
                        if (isChecked) ThemeUtils.setTheme(ctx, ThemeUtils.DARK)
                        else ThemeUtils.setTheme(ctx, ThemeUtils.LIGHT)
                        act.recreate()
                        //ThemeUtils.restartApp(context)
                    }
                }

                /** Setting menu onClick listener */
                btnSetting.setOnClickListener { _ ->
                    when (it.type) {
                        Setting.Type.LANGUAGE -> {

                            val languageCodes = ctx.resources.getStringArray(R.array.languages_code)

                            MaterialDialog(ctx).show {
                                title(R.string.title_language)
                                cornerRadius(16f)
                                listItems(R.array.languages_name) { _, index, text ->
                                    sharedPreferences?.let { sharedPref ->
                                        LocaleHelper.loadLocal(context)
                                        sharedPref.edit().putString(Preferences.LANGUAGE, text.toString()).apply()
                                        sharedPref.edit().putString(Preferences.LANGUAGE_CODE, languageCodes[index]).apply()
                                    }

                                    it.subTitle = text.toString()
                                    adapter.notifyItemChanged(position)
                                    act.recreate()
                                }
                            }
                        }
                        Setting.Type.THEME -> {
                            btnSwitch.isChecked = !btnSwitch.isChecked
                        }
                        Setting.Type.CLEAR_CACHE -> {
                            FileHelper.clearDirectory(ctx.cacheDir)
                            PopupUtils.showToast(ctx, ctx.resources?.getString(R.string.msg_clear_cache), Toast.LENGTH_SHORT)

                            val cacheSize = (FileHelper.getDirectorySize(ctx.cacheDir) / FileHelper.MB).toDouble()
                            val formatter = DecimalFormat("#0.00")

                            it.content = ctx.resources.getString(R.string.total_cache) + " ${formatter.format(cacheSize)} MB"

                            adapter.notifyItemChanged(position)

                        }
                        Setting.Type.WALLPAPER_PREVIEW_QUALITY -> {

                            MaterialDialog(ctx).show {
                                title(R.string.title_wallpaper_preview_quality)
                                cornerRadius(16f)
                                listItems(R.array.wallpaper_quality) { _, _, text ->

                                    // Check if user select HIGH quality
                                    if (text == "Raw" || text == "Full") {

                                        MaterialDialog(context).show {
                                            title(R.string.title_warning)
                                            cornerRadius(16f)
                                            message(R.string.desc_quality_warn_dialog)
                                            positiveButton(R.string.yes) { warnDialog ->

                                                sharedPreferences!!.edit().putString(Preferences.WALLPAPER_QUALITY, text.toString()).apply()
                                                setting.subTitle = text.toString()
                                                adapter.notifyItemChanged(position)

                                                warnDialog.dismiss()
                                            }

                                            negativeButton(R.string.no) { warnDialog ->
                                                warnDialog.dismiss()
                                            }
                                        }

                                    } else {

                                        sharedPreferences!!.edit().putString(Preferences.WALLPAPER_QUALITY, text.toString()).apply()
                                        setting.subTitle = text.toString()
                                        adapter.notifyItemChanged(position)

                                    }

                                }
                            }

                        }
                        Setting.Type.RESET_TUTORIAL -> {
                            sharedPreferences!!.edit().putBoolean(Preferences.SHOW_INTRO_TUTORIAL, true).apply()
                            PopupUtils.showToast(ctx, ctx.resources.getString(R.string.msg_reset_tutorial), Toast.LENGTH_SHORT)
                        }
                        Setting.Type.UNSPLASH_SITE -> {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.subTitle))
                            ctx.startActivity(intent)
                        }
                        else -> {}
                    }
                }

            }
        }
    }

    class Footer(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

}