package com.highfive.app.android

import android.content.Context

fun Context.showCustomAlertDialog(
    title: String,
    message: String,
    positiveButtonText: String,
    negativeButtonText: String? = null,
    onPositiveClick: (() -> Unit)?= null,
    onNegativeClick: (() -> Unit)? = null
) {
    val builder = androidx.appcompat.app.AlertDialog.Builder(this)
    builder.setTitle(title)
    builder.setMessage(message)
    if (positiveButtonText != null && onPositiveClick != null) {
        builder.setPositiveButton(positiveButtonText) { dialog, _ ->
            onPositiveClick()
            dialog.dismiss()
        }
    }
    if (negativeButtonText != null && onNegativeClick != null) {
        builder.setNegativeButton(negativeButtonText) { dialog, _ ->
            onNegativeClick()
            dialog.dismiss()
        }
    }
    val dialog = builder.create()
    dialog.show()
}
