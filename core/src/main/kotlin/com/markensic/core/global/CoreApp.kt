package com.markensic.core.global

import android.app.Activity
import android.app.Application
import android.content.pm.ApplicationInfo
import com.markensic.core.global.log.CoreLog
import com.markensic.core.global.stack.ActivityCallbacks

object CoreApp {

  private var activityCallbacks: ActivityCallbacks? = null

  @Volatile
  private var _a: Application? = null
  val sApplication: Application
    get() {
      /**
       * @discard
       * if (_a == null) {
       *   synchronized(CoreApp::class) {
       *     if (_a == null) {
       *       val at = Class.forName("android.app.ActivityThread")
       *       _a = at.getDeclaredMethod("currentApplication").invoke(null) as Application
       *     }
       *   }
       * }
       */
      return _a ?: throw IllegalStateException("CoreApp has not been initialized!")
    }

  val isDebug = _a?.applicationInfo?.flags?.and(ApplicationInfo.FLAG_DEBUGGABLE) != 0

  val topActivity: Activity
    get() {
      if (_a == null) {
        throw IllegalStateException("CoreApp has not been initialized!")
      }
      return activityCallbacks?.topActivity() ?: throw RuntimeException("Activity not create or application not registerActivityLifecycleCallbacks")
    }

  fun initCore(a: Application, isCatchCrash: Boolean = true, listener: CrashHandler.UploadListener? = null) {
    if (_a == null) {
      synchronized(CoreApp::class) {
        if (_a == null) {
          _a = a

          if (isDebug) {
            CoreLog.initLog()
          }

          activityCallbacks = ActivityCallbacks(isDebug)

          _a!!.registerActivityLifecycleCallbacks(activityCallbacks)

          if (isCatchCrash) {
            CrashHandler.init()
            CrashHandler.upLoadCrashListener = listener
          }
        }
      }
    } else {
      CoreLog.d("The CoreApp is Initialized")
    }
  }
}
