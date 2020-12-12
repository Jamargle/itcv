package com.jmlb0003.itcv.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.jmlb0003.itcv.R

private const val LOADING_DIALOG_TAG = "Tag:LoadingDialogFragment"

internal class LoadingDialog : DialogFragment() {

    init {
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.loading_dialog, container, false)

    fun showLoading(supportFragmentManager: FragmentManager, tag: String = LOADING_DIALOG_TAG) {
        supportFragmentManager.showLoading(tag)
    }

    fun hideLoading(tag: String = LOADING_DIALOG_TAG) {
        try {
            parentFragmentManager.hideLoadingIfExists(tag)
        } catch (exception: IllegalStateException) {
            // NO-OP
        }
    }
}

fun FragmentActivity.showLoading(tag: String = LOADING_DIALOG_TAG) {
    supportFragmentManager.showLoading(tag)
}

fun FragmentManager.showLoading(tag: String = LOADING_DIALOG_TAG) {
    if (findFragmentByTag(tag) != null) {
        // If the loading is already shown, keep it
        return
    }
    LoadingDialog().show(this, tag)
}

fun FragmentActivity.hideLoadingIfExists(tag: String = LOADING_DIALOG_TAG) {
    supportFragmentManager.hideLoadingIfExists(tag)
}

fun FragmentManager.hideLoadingIfExists(tag: String = LOADING_DIALOG_TAG) {
    findFragmentByTag(tag)?.let { existingLoading ->
        (existingLoading as? LoadingDialog)?.dismissAllowingStateLoss()
    }
}
