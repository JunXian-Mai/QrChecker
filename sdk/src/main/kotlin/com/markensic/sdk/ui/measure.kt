package com.markensic.sdk.ui

import android.view.View
import android.view.ViewGroup

fun Int.toExactlyMeasureSpec() =
  View.MeasureSpec.makeMeasureSpec(this, View.MeasureSpec.EXACTLY)

fun Int.toAtMostMeasureSpec() =
  View.MeasureSpec.makeMeasureSpec(this, View.MeasureSpec.AT_MOST)


@Throws(IllegalAccessError::class)
fun View.defaultMeasureWidth(parentView: ViewGroup, checkZero: Boolean = true): Int {
  return when (layoutParams.width) {
    ViewGroup.LayoutParams.MATCH_PARENT -> parentView.measuredWidth.toExactlyMeasureSpec()
    ViewGroup.LayoutParams.WRAP_CONTENT -> parentView.measuredWidth.toAtMostMeasureSpec()
    0 -> if (checkZero) {
      throw IllegalAccessError("Need special treatment for $this")
    } else {
      0.toExactlyMeasureSpec()
    }
    else -> layoutParams.width.toExactlyMeasureSpec()
  }
}

@Throws(IllegalAccessError::class)
fun View.defaultMeasureHeight(parentView: ViewGroup, checkZero: Boolean = true): Int {
  return when (layoutParams.height) {
    ViewGroup.LayoutParams.MATCH_PARENT -> parentView.measuredHeight.toExactlyMeasureSpec()
    ViewGroup.LayoutParams.WRAP_CONTENT -> parentView.measuredHeight.toAtMostMeasureSpec()
    0 -> if (checkZero) {
      throw IllegalAccessError("Need special treatment for $this")
    } else {
      0.toExactlyMeasureSpec()
    }
    else -> layoutParams.height.toExactlyMeasureSpec()
  }
}

fun ViewGroup.autoMeasureChildView(childView: View, checkZero: Boolean = true) {
  childView.measure(
    childView.defaultMeasureWidth(this, checkZero),
    childView.defaultMeasureHeight(this, checkZero)
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