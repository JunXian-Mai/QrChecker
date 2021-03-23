package com.markensic.sdk.global

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.ref.WeakReference
import java.util.*

class ActivityStack : Application.ActivityLifecycleCallbacks {
  val stack: Stack<WeakReference<Activity>> = Stack()

  override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    stack.push(WeakReference(activity))
  }

  override fun onActivityStarted(activity: Activity) {
  }

  override fun onActivityResumed(activity: Activity) {
  }

  override fun onActivityPaused(activity: Activity) {
  }

  override fun onActivityStopped(activity: Activity) {
  }

  override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
  }

  override fun onActivityDestroyed(activity: Activity) {
    stack.pop()
  }
}