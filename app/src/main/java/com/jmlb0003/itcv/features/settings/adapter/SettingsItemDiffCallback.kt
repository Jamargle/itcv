package com.jmlb0003.itcv.features.settings.adapter

import androidx.recyclerview.widget.DiffUtil

object SettingsItemDiffCallback : DiffUtil.ItemCallback<SettingsItem>() {

    override fun areItemsTheSame(oldItem: SettingsItem, newItem: SettingsItem) =
        oldItem.label == newItem.label

    override fun areContentsTheSame(oldItem: SettingsItem, newItem: SettingsItem) =
        oldItem.label == newItem.label
}
