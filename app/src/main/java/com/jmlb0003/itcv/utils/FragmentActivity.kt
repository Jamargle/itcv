package com.jmlb0003.itcv.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentActivity

object FragmentActivity {

    /**
     * It requests focus to the `viewToBeFocused` and if succeeded it tries to display the software
     * keyboard attached to it.
     */
    fun FragmentActivity.showSoftKeyboard(viewToBeFocused: View) {
        if (viewToBeFocused.requestFocus()) {
            (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.let {
                it.showSoftInput(viewToBeFocused, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }
}
