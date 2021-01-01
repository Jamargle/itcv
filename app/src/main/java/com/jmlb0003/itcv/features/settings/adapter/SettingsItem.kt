package com.jmlb0003.itcv.features.settings.adapter

import androidx.annotation.StringRes
import com.jmlb0003.itcv.R

sealed class SettingsItem(@StringRes val label: Int) {
    object About : SettingsItem(R.string.settings_about)
}
