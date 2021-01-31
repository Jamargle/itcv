package com.jmlb0003.itcv.core

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View =
    LayoutInflater.from(context).inflate(layoutRes, this, false)

fun FragmentActivity.tryToOpenUrl(url: String) {
    Intent(Intent.ACTION_VIEW, Uri.parse(url)).let { browserIntent ->
        if (browserIntent.resolveActivity(packageManager) != null) {
            startActivity(browserIntent)
        }
    }
}
