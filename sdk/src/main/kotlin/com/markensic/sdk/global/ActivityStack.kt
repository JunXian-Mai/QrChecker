package com.markensic.sdk.global

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.ref.WeakReference
import java.util.*

class ActivityStack(private val isPrintLifeState: Boolean = false) : Application.ActivityLifecycleCallbacks {
  val stack: Stack<WeakReference<Activity>> = Stack()

  override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    stack.push(WeakReference(activity))
    if (isPrintLifeState) activity.sdkLogd("onActivityCreated")
  }

  override fun onActivityStarted(activity: Activity) {
    if (isPrintLifeState) activity.sdkLogd("onActivityStarted")
  }

  override fun onActivityResumed(activity: Activity) {
    if (isPrintLifeState) activity.sdkLogd("onActivityResumed")
  }

  override fun onActivityPaused(activity: Activity) {
    if (isPrintLifeState) activity.sdkLogd("onActivityPaused")
  }

  override fun onActivityStopped(activity: Activity) {
    if (isPrintLifeState) activity.sdkLogd("onActivityStopped")
  }

  override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    if (isPrintLifeState) activity.sdkLogd("onActivitySaveInstanceState")
  }

  override fun onActivityDestroyed(activity: Activity) {
    stack.pop()
    if (isPrintLifeState) activity.sdkLogd("onActivityDestroyed")
  }
}