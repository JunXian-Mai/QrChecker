package com.markensic.sdk.global

import android.app.Activity
import android.app.Application

object App {

  @Volatile
  private var _a: Application? = null
  val sApplication: Application
    get() {
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

  fun initStackContext(c: LibStackContext) {
    sApplication?.registerActivityLifecycleCallbacks(c.getActivityStack())
  }

  var currentActivity: Activity? = null
    get() {
      return if (sApplication is LibStackContext) {
        (sApplication as LibStackContext).getActivityStack().stack.peek().get()
          ?: throw RuntimeException("Activity Not Create Or Application Not registerActivityLifecycleCallbacks")
      } else {
        throw IllegalArgumentException("Activity Not Found, Application Should Implement LibStackContext")
      }
    }
}