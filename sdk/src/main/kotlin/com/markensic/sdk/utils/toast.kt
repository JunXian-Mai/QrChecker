package com.markensic.sdk.utils

import android.content.Context
import android.widget.Toast
import com.markensic.sdk.global.App

fun Context.showToast(message: String) {
  Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}