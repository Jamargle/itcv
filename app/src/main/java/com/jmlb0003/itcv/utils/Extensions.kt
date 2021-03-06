package com.jmlb0003.itcv.utils

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.jmlb0003.itcv.R

fun FragmentActivity.showErrorPopup(
    errorTitle: String = resources.getString(R.string.error_dialog_title),
    errorMessage: String = resources.getString(R.string.error_dialog_generic_message),
    isCancelable: Boolean = false,
    positiveButtonText: String = resources.getString(R.string.error_dialog_positive_button),
    negativeButtonText: String = resources.getString(R.string.error_dialog_negative_button),
    onPositiveButtonClicked: (() -> Unit)? = null,
    onNegativeButtonClicked: (() -> Unit)? = null
) {
    val builder = AlertDialog.Builder(this).apply {
        setCancelable(isCancelable)
        if (errorTitle.isNotBlank()) {
            setTitle(errorTitle)
        }
        if (errorMessage.isNotBlank()) {
            setMessage(errorMessage)
        }
        if (onPositiveButtonClicked != null) {
            setPositiveButton(positiveButtonText) { dialog, _ ->
                dialog.dismiss()
                onPositiveButtonClicked()
            }
        }
        if (onNegativeButtonClicked != null) {
            setNegativeButton(negativeButtonText) { dialog, _ ->
                dialog.dismiss()
                onNegativeButtonClicked()
            }
        }
    }
    val dialog = builder.create()

    lifecycle.addObserver(object : LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun displayDialog() {
            dialog.show()
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun dismissDialog() {
            dialog.dismiss()
        }
    })
}
