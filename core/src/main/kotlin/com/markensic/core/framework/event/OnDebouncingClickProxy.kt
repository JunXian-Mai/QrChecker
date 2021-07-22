package com.markensic.core.framework.event

import android.view.View
import com.markensic.core.global.log.CoreLog

class OnDebouncingClickProxy(
  val isGlobal: Boolean = false,
  val duration: Long = DEFAULT_DURATION,
  val proxy: (View) -> Unit
) : View.OnClickListener {

  companion object {
    val DEFAULT_DURATION: Long = 750

    @Volatile
    private var sClickable = true
    private val sAllowClick = {
      sClickable = true
    }
  }

  private var tag: Any? = null

  private val isValid: Boolean
    get() {
      val curTime = System.currentTimeMillis()
      if (tag !is Long) {
        tag = curTime
        return true
      }
      val preTime = tag as Long
      return if (curTime - preTime <= duration) {
        false
      } else {
        tag = curTime
        true
      }
    }

  override fun onClick(v: View) {
    if (isGlobal) {
      if (sClickable) {
        sClickable = false
        v.postDelayed(sAllowClick, duration)
        proxy(v)
      } else {
        CoreLog.i("This onClick be intercepted by OnDebouncingClickProxy")
      }
    } else {
      if (isValid) {
        proxy(v)
      } else {
        CoreLog.i("This onClick be intercepted by OnDebouncingClickProxy")
      }
    }
  }
}

fun View.applyOnDebouncingClickProxy(
  isGlobal: Boolean = false,
  duration: Long = OnDebouncingClickProxy.DEFAULT_DURATION,
  listener: (View) -> Unit
) {
  setOnClickListener(OnDebouncingClickProxy(isGlobal, duration, listener))
}

fun Array<View>.applyOnDebouncingClickProxy(
  isGlobal: Boolean = false,
  duration: Long = OnDebouncingClickProxy.DEFAULT_DURATION,
  listener: (View) -> Unit
) {
  forEach {
    it.applyOnDebouncingClickProxy(isGlobal, duration, listener)
  }
}
