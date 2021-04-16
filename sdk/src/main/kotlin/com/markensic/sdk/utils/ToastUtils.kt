package com.markensic.sdk.utils

import android.widget.Toast
import com.markensic.sdk.global.App

object ToastUtils {
  fun show(msg: String) {
    Toast.makeText(App.sApplication, msg, Toast.LENGTH_SHORT).show()
  }
}