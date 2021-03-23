package com.markensic.sdk.ui

import android.view.View
import android.view.ViewGroup

fun Int.toExactlyMeasureSpec() =
  View.MeasureSpec.makeMeasureSpec(this, View.MeasureSpec.EXACTLY)

fun Int.toAtMostMeasureSpec() =
  View.MeasureSpec.makeMeasureSpec(this, View.MeasureSpec.AT_MOST)


fun View.defaultMeasureWidth(parentView: ViewGroup): Int {
  return when (layoutParams.width) {
    ViewGroup.LayoutParams.MATCH_PARENT -> parentView.measuredWidth.toExactlyMeasureSpec()
    ViewGroup.LayoutParams.WRAP_CONTENT -> parentView.measuredWidth.toAtMostMeasureSpec()
    0 -> throw IllegalAccessError("Need special treatment for $this")
    else -> layoutParams.width.toExactlyMeasureSpec()
  }
}

fun View.defaultMeasureHeight(parentView: ViewGroup): Int {
  return when (layoutParams.height) {
    ViewGroup.LayoutParams.MATCH_PARENT -> parentView.measuredHeight.toExactlyMeasureSpec()
    ViewGroup.LayoutParams.WRAP_CONTENT -> parentView.measuredHeight.toAtMostMeasureSpec()
    0 -> throw IllegalAccessError("Need special treatment for $this")
    else -> layoutParams.width.toExactlyMeasureSpec()
  }
}

fun ViewGroup.autoMeasureChildView(childView: View) {
  childView.measure(
    childView.defaultMeasureWidth(this),
    childView.defaultMeasureHeight(this)
  )
}

fun ViewGroup.layoutChildView(childView: View, x: Int, y: Int, fromLeft: Boolean = true) {
  if (fromLeft) {
    childView.layout(
      x,
      y,
      x + childView.measuredWidth,
      y + childView.measuredHeight
    )
  } else {
    layoutChildView(
      childView,
      measuredWidth - x - childView.measuredWidth,
      y
    )
  }
}