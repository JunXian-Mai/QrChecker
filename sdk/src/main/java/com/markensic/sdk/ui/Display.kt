package com.markensic.sdk.ui

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import com.markensic.sdk.global.App.sApplication

object Display {

  @Volatile
  private var _physicsDm: DisplayMetrics? = null

  val physicsDm: DisplayMetrics
    get() {
      if (_physicsDm == null) {
        synchronized(Display::class) {
          if (_physicsDm == null) {
            _physicsDm = DisplayMetrics()
            val windowManager = sApplication.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = Class.forName("android.view.Display")
            display
              .getMethod("getRealMetrics", DisplayMetrics::class.java)
              .invoke(windowManager.defaultDisplay, _physicsDm)
          }
        }
      }
      return _physicsDm ?: throw IllegalArgumentException("DisplayMetrics Not Initiation ")
    }

  val realDensity = physicsDm.density

  val realDensityDpi = physicsDm.densityDpi

  val realWidth = physicsDm.widthPixels

  val realHeight = physicsDm.heightPixels
}