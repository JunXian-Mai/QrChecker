package com.markensic.sdk.utils

import android.content.ContextWrapper
import com.markensic.sdk.global.App
import java.io.InputStream

fun <R> ContextWrapper.openAssetsFile(filename: String, block: (InputStream) -> R) =
  assets.open(filename).use(block)