package com.jmlb0003.itcv.features.settings.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jmlb0003.itcv.R

class SettingsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(configurationItem: SettingsItem) {
        with(itemView) {
            findViewById<TextView>(R.id.settings_item_label)?.let {
                it.text = context.getString(configurationItem.label)
            }
        }
    }
}
