package com.dilipsuthar.wallbox.preferences

import android.content.Context
import android.content.SharedPreferences
/**
 * modified by Dilip Suthar on 15/12/19
 */
class SharedPref constructor(context: Context){

    private val sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    // Static member & methods
    companion object {
        var instance: SharedPref? = null

        fun getInstance(context: Context): SharedPref {
            if (instance == null) {
                instance = SharedPref(context)
            }
            return instance as SharedPref
        }
    }

    fun getSharedPreferences(): SharedPreferences = sharedPreferences

    /**
     * @func get string value from shared preference
     * @param key key for value
     * @param default default value to return
     *
     * @return return string value for given key
     */
    fun getString(key: String, default: String? = ""): String? {
        return sharedPreferences.getString(key, default)
    }

    /**
     * @func get int value from shared preference
     * @param key key for value
     * @param default default value to return
     *
     * @return return int value for given key
     */
    fun getInt(key: String, default: Int = -1): Int {
        return sharedPreferences.getInt(key, default)
    }

    /**
     * @func get boolean value from shared preference
     * @param key key for value
     * @param default default value to return
     *
     * @return return boolean value for given key
     */
    fun getBoolean(key: String, default: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, default)
    }

    /**
     * @func save data into shared preference
     * @param key key for value
     * @param value value to save into shared preference
     */
    fun saveData(key: String, value: Any) {
        when (value) {
            is Boolean -> sharedPreferences.edit().putBoolean(key, value).apply()
            is String -> sharedPreferences.edit().putString(key, value).apply()
            is Int -> sharedPreferences.edit().putInt(key, value).apply()
        }
    }
}