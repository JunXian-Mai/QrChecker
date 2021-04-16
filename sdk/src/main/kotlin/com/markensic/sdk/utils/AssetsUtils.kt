package com.markensic.sdk.utils

import android.content.ContextWrapper
import com.markensic.sdk.global.App
import java.io.InputStream

object AssetsUtils {
  fun <R> open(cw: ContextWrapper = App.sApplication, name: String, block: (InputStream) -> R): R {
    return cw.assets.open(name).use(block)
  }
}