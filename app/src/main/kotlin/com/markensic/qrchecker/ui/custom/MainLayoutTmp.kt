package com.markensic.qrchecker.ui.custom

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.widget.Button
import android.widget.TextView
import androidx.core.view.*
import com.markensic.qrchecker.R
import com.markensic.sdk.ui.*

class MainLayoutTmp @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CustomLayout(context, attrs, defStyleAttr) {

  val contentHeight = Display.realHeight - Ui.statusBarSize - Ui.navigationBarSize

  val statusBarTv = TextView(context).apply {
    background = ColorDrawable(resources.getColor(R.color.purple_200, null))
    gravity = Gravity.CENTER_VERTICAL
    text = "状态栏"
    addView(this, matchParent, Ui.statusBarSize)
  }

  val contentUpTv = TextView(context).apply {
    background = ColorDrawable(resources.getColor(R.color.teal_200, null))
    text = "Hello World!"
    addView(this, matchParent, contentHeight / 2)
  }

  val showTv = TextView(context).apply {
    background = ColorDrawable(resources.getColor(R.color.teal_200, null))
    addView(this, matchParent, wrapContent) {
      updatePadding(left = 5.dp2Int, right = 5.dp2Int)
    }
  }

  val nextEvent = Button(context).apply {
    background = ColorDrawable(resources.getColor(R.color.white, null))
    text = "to next page"
    addView(this, wrapContent, wrapContent) {
      updatePadding(left = 5.dp2Int, right = 5.dp2Int)
      updateMargins(left = 5.dp2Int, top = 5.dp2Int)
    }
  }

  val event = Button(context).apply {
    background = ColorDrawable(resources.getColor(R.color.white, null))
    text = "change SharedViewModel.name"
    addView(this, wrapContent, wrapContent) {
      updatePadding(left = 5.dp2Int, right = 5.dp2Int)
      updateMargins(left = 5.dp2Int, top = 5.dp2Int)
    }
  }

  val eventTv = TextView(context).apply {
    background = ColorDrawable(resources.getColor(R.color.teal_200, null))
    addView(this, matchParent, wrapContent) {
      updatePadding(left = 5.dp2Int, right = 5.dp2Int)
      updateMargins(left = 5.dp2Int, top = 5.dp2Int)
    }
  }

  val contentDownTv = TextView(context).apply {
    background = ColorDrawable(resources.getColor(R.color.teal_700, null))
    gravity = Gravity.BOTTOM
    text = "Hello World!"
    addView(this, matchParent, contentHeight - (contentHeight / 2))
  }

  val navigationBarTv = TextView(context).apply {
    background = ColorDrawable(resources.getColor(R.color.purple_700, null))
    gravity = Gravity.CENTER
    text = "导航栏"
    addView(this, matchParent, Ui.navigationBarSize)
  }

  override fun onMeasureChildren(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    forEachAutoMeasure()
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    statusBarTv.layout(0,0)
    contentUpTv.layout(0, statusBarTv.below)
    showTv.layout(0, contentUpTv.alignTop)
    nextEvent.layout(0, showTv.below)
    event.layout(0, nextEvent.below)
    eventTv.layout(0, event.below)
    contentDownTv.layout(0, contentUpTv.below)
    navigationBarTv.layout(0, contentDownTv.below)
  }
}
