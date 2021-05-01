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

    protected fun getString(@StringRes key: Int, defaultValue: String? = "") =
        sharedPreferences.getString(getPrefKey(key), defaultValue)

    protected fun setString(@StringRes key: Int, value: String) =
        sharedPreferences.edit().putString(getPrefKey(key), value).apply()

    private fun getPrefKey(@StringRes key: Int) = context.getString(key)
}
