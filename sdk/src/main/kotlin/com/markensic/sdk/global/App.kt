package com.markensic.sdk.global

import android.app.Activity
import android.app.Application
import android.content.pm.ApplicationInfo

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

  val isDebug = _a?.applicationInfo?.flags?.and(ApplicationInfo.FLAG_DEBUGGABLE) != 0

  var currentActivity: Activity? = null
    get() {
      return if (_a is LibStackContext) {
        (_a as LibStackContext).getActivityStack().stack.peek().get()
          ?: throw RuntimeException("Activity Not Create Or Application Not registerActivityLifecycleCallbacks")
      } else {
        throw IllegalArgumentException("Activity Not Found, Application Should Implement LibStackContext")
      }
    }

  fun initApplication(a: Application) {
    if (_a != a) {
      synchronized(App::class) {
        if (_a != a) {
          _a = a
        }
      }
    }
    if (a is LibStackContext) {
      _a?.registerActivityLifecycleCallbacks(a.getActivityStack())
    }
    CrashHandler.init()
  }
}