package com.markensic.core.framework.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

fun Bitmap.toByteArray(): ByteArray {
  val baos = ByteArrayOutputStream().apply {
    compress(Bitmap.CompressFormat.PNG, 100, this)
  }
  return baos.toByteArray()
}

fun Bitmap.compressInWidth(targetDensity: Int): Bitmap {
  val opt = BitmapFactory.Options()
  opt.inJustDecodeBounds = true
  val bytes = this.toByteArray()
  BitmapFactory.decodeByteArray(bytes, 0, bytes.size, opt)
  opt.inJustDecodeBounds = false
  opt.inDensity = opt.outWidth
  opt.inTargetDensity = targetDensity
  return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, opt)
}
