package org.ktc2.cokaen.wouldyouin.core

import android.content.Context
import android.widget.Toast

object ToastUtils {
    fun showShortToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showLongToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}