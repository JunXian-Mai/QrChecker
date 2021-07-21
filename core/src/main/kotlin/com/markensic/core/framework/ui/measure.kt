package com.markensic.core.framework.ui

import android.graphics.Color
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.ViewGroup
import com.markensic.core.global.log.CoreLog

const val matchParent = ViewGroup.LayoutParams.MATCH_PARENT

const val wrapContent = ViewGroup.LayoutParams.WRAP_CONTENT

fun View.transparentBackground() {
  setBackgroundColor(Color.TRANSPARENT)
}

val View.parentView get() = parent as ViewGroup

fun View?.performHapticFeedbackSafely() {
  try {
    this?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
  } catch (t: Throwable) {
    CoreLog.e(t)
  }
}
