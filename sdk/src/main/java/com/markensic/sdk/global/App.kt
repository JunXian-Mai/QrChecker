package com.markensic.sdk.global

import android.app.Application
import java.lang.IllegalArgumentException

object App {

  @Volatile private var _a: Application? = null

  val sApplication: Application get() {
    if (_a == null) {
      synchronized(App::class) {
        if (_a == null) {
          val at = Class.forName("android.app.ActivityThread")
          _a = at.getDeclaredMethod("currentApplication").invoke(null) as Application
        }
      }
    }
    return _a ?: throw IllegalArgumentException("Application Not Found")
  }
}