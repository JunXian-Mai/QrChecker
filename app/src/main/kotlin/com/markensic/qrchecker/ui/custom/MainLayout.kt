package com.markensic.qrchecker.ui.custom

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.updatePadding
import com.markensic.qrchecker.R
import com.markensic.sdk.ui.*

class MainLayout(context: Context) : ViewGroup(context) {

  val contentHeight = Display.realHeight - Ui.statusBarSize - Ui.navigationBarSize

  val statusBarTv = TextView(context).apply {
    layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, Ui.statusBarSize)
    background = ColorDrawable(resources.getColor(R.color.purple_200, null))
    gravity = Gravity.CENTER_VERTICAL
    text = "状态栏"
    addView(this)
  }

  val contentUpTv = TextView(context).apply {
    layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, contentHeight / 2)
    background = ColorDrawable(resources.getColor(R.color.teal_200, null))
    text = "Hello World!"
    addView(this)
  }

  val showTv = TextView(context).apply {
    layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    background = ColorDrawable(resources.getColor(R.color.teal_200, null))
    updatePadding(left = 5.dp.toInt(), right = 5.dp.toInt())
    addView(this)
  }

  val nextEvent = Button(context).apply {
    layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    background = ColorDrawable(resources.getColor(R.color.white, null))
    text = "to next page"
    updatePadding(left = 5.dp.toInt(), right = 5.dp.toInt())
    addView(this)
  }

  val event = Button(context).apply {
    layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    background = ColorDrawable(resources.getColor(R.color.white, null))
    text = "change SharedViewModel.name"
    updatePadding(left = 5.dp.toInt(), right = 5.dp.toInt())
    addView(this)
  }

  val eventTv = TextView(context).apply {
    layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    background = ColorDrawable(resources.getColor(R.color.teal_200, null))
    updatePadding(left = 5.dp.toInt(), right = 5.dp.toInt())
    addView(this)
  }

  val contentDownTv = TextView(context).apply {
    layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, contentHeight - (contentHeight / 2))
    background = ColorDrawable(resources.getColor(R.color.teal_700, null))
    gravity = Gravity.BOTTOM
    text = "Hello World!"
    addView(this)
  }

  val navigationBarTv = TextView(context).apply {
    layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, Ui.navigationBarSize)
    background = ColorDrawable(resources.getColor(R.color.purple_700, null))
    gravity = Gravity.CENTER
    text = "导航栏"
    addView(this)
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    autoMeasureChildView(statusBarTv)
    autoMeasureChildView(contentUpTv)
    autoMeasureChildView(showTv)
    autoMeasureChildView(nextEvent)
    autoMeasureChildView(event)
    autoMeasureChildView(eventTv)
    autoMeasureChildView(contentDownTv)
    autoMeasureChildView(navigationBarTv, false)
    setMeasuredDimension(measuredWidth, measuredHeight)
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    layoutChildView(statusBarTv, 0, 0)
    layoutChildView(contentUpTv, 0, statusBarTv.bottom)
    layoutChildView(showTv, 0, contentUpTv.top)
    layoutChildView(nextEvent, 5.dp.toInt(), showTv.bottom + 5.dp.toInt())
    layoutChildView(event, 5.dp.toInt(), nextEvent.bottom + 5.dp.toInt())
    layoutChildView(eventTv, 5.dp.toInt(), event.bottom + 5.dp.toInt())
    layoutChildView(contentDownTv, 0, contentUpTv.bottom)
    layoutChildView(navigationBarTv, 0, contentDownTv.bottom)
  }
}