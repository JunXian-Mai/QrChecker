package com.markensic.sdk.global

import android.app.Activity
import android.app.Application
import android.content.pm.ApplicationInfo
import com.markensic.sdk.global.log.CoreLog


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
      return _a ?: throw IllegalArgumentException("Application not be create")
    }

  val isDebug = _a?.applicationInfo?.flags?.and(ApplicationInfo.FLAG_DEBUGGABLE) != 0

  val currentActivity: Activity
    get() {
      return if (_a is LibStackContext) {
        (_a as LibStackContext).activityStack.stack.peek().get()
          ?: throw RuntimeException("Activity not create or application not registerActivityLifecycleCallbacks")
      } else {
        throw IllegalArgumentException("Activity not found, application should implement LibStackContext")
      }
    }

  fun initApplication(a: Application, isCatchCrash: Boolean = true, listener: CrashHandler.UploadListener? = null) {
    if (_a != a) {
      synchronized(App::class) {
        if (_a != a) {
          _a = a
        }
      }
    }

    if (isDebug) {
      CoreLog.initLog()
    }

    if (a is LibStackContext) {
      _a?.registerActivityLifecycleCallbacks(a.activityStack)
    }

    if (isCatchCrash) {
      CrashHandler.init()
      CrashHandler.upLoadCrashListener = listener
    }
  }
}
