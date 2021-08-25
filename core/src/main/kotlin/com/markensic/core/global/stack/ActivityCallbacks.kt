package com.markensic.core.global.stack

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.SparseArray
import androidx.core.util.set
import com.markensic.core.global.log.CoreLog
import java.lang.ref.WeakReference

internal class ActivityCallbacks(private val isPrintLifeState: Boolean = false) : Application.ActivityLifecycleCallbacks {

  private val activitySparseArray = SparseArray<WeakReference<Activity>>()

  override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    activitySparseArray[activity.hashCode()] = WeakReference(activity)
    if (isPrintLifeState) CoreLog.d("${activity.javaClass.simpleName} onActivityCreated")
  }

  override fun onActivityStarted(activity: Activity) {
    if (isPrintLifeState) CoreLog.d("${activity.javaClass.simpleName} onActivityStarted")
  }

  override fun onActivityResumed(activity: Activity) {
    if (isPrintLifeState) CoreLog.d("${activity.javaClass.simpleName} onActivityResumed")
  }

  override fun onActivityPaused(activity: Activity) {
    if (isPrintLifeState) CoreLog.d("${activity.javaClass.simpleName} onActivityPaused")
  }

  override fun onActivityStopped(activity: Activity) {
    if (isPrintLifeState) CoreLog.d("${activity.javaClass.simpleName} onActivityStopped")
  }

  override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    if (isPrintLifeState) CoreLog.d("${activity.javaClass.simpleName} onActivitySaveInstanceState")
  }

  override fun onActivityDestroyed(activity: Activity) {
    activitySparseArray.remove(activity.hashCode())
    if (isPrintLifeState) CoreLog.d("${activity.javaClass.simpleName} onActivityDestroyed")
  }

  fun topActivity() = activitySparseArray.valueAt(activitySparseArray.size() - 1).get()
}
