package com.jmlb0003.itcv.features.settings.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.jmlb0003.itcv.R
import com.jmlb0003.itcv.core.inflate

class SettingsAdapter(
    private val onSettingsItemClicked: (SettingsItem) -> Unit
) : ListAdapter<SettingsItem, SettingsViewHolder>(SettingsItemDiffCallback) {

    fun setSettingsItems(items: List<SettingsItem>) {
        submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SettingsViewHolder(parent.inflate(R.layout.list_item_settings))

    override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener { onSettingsItemClicked(getItem(position)) }
    }
}
