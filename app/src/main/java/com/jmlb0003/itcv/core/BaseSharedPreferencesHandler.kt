package com.jmlb0003.itcv.core

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StringRes
import androidx.preference.PreferenceManager

abstract class BaseSharedPreferencesHandler(
    private val context: Context
) {

    private val sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    protected fun getBoolean(@StringRes key: Int, defaultValue: Boolean = false) =
        sharedPreferences.getBoolean(getPrefKey(key), defaultValue)

    protected fun setBoolean(@StringRes key: Int, value: Boolean) {
        sharedPreferences.edit().putBoolean(getPrefKey(key), value).apply()
    }

    private fun getPrefKey(@StringRes key: Int) = context.getString(key)
}
