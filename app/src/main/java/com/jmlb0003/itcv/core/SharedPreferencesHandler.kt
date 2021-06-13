package com.jmlb0003.itcv.core

import android.content.Context
import com.jmlb0003.itcv.R
import javax.inject.Inject

class SharedPreferencesHandler
@Inject constructor(
    context: Context
) : BaseSharedPreferencesHandler(context) {

    var defaultUserName: String
        get() = getString(R.string.pref_default_github_username) ?: ""
        set(value) {
            setString(R.string.pref_default_github_username, value)
        }
}
