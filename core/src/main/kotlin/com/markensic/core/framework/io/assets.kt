package com.markensic.core.utils

import android.content.ContextWrapper
import java.io.InputStream

fun <R> ContextWrapper.openAssetsFile(filename: String, block: (InputStream) -> R) =
  assets.open(filename).use(block)
