package com.markensic.core.global.stack

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.markensic.core.global.log.CoreLog
import java.lang.ref.WeakReference
import java.util.*

class ActivityStack(private val isPrintLifeState: Boolean = false) : Application.ActivityLifecycleCallbacks {
  val stack: Stack<WeakReference<Activity>> = Stack()

  override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    stack.push(WeakReference(activity))
    if (isPrintLifeState) CoreLog.d("onActivityCreated")
  }

  override fun onActivityStarted(activity: Activity) {
    if (isPrintLifeState) CoreLog.d("onActivityStarted")
  }

  override fun onActivityResumed(activity: Activity) {
    if (isPrintLifeState) CoreLog.d("onActivityResumed")
  }

  override fun onActivityPaused(activity: Activity) {
    if (isPrintLifeState) CoreLog.d("onActivityPaused")
  }

  override fun onActivityStopped(activity: Activity) {
    if (isPrintLifeState) CoreLog.d("onActivityStopped")
  }

  override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    if (isPrintLifeState) CoreLog.d("onActivitySaveInstanceState")
  }

  override fun onActivityDestroyed(activity: Activity) {
    stack.pop()
    if (isPrintLifeState) CoreLog.d("onActivityDestroyed")
  }
}
