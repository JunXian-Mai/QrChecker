package com.markensic.sdk.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.*

@Suppress("MemberVisibilityCanBePrivate")
abstract class CustomLayout @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

  class LayoutParams(width: Int, height: Int) : MarginLayoutParams(width, height)

  override fun generateDefaultLayoutParams(): LayoutParams {
    return LayoutParams(matchParent, wrapContent)
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    this.onMeasureChildren(widthMeasureSpec, heightMeasureSpec)
    // todo 子控件绘制完 重新setMeasuredDimension
    setMeasuredDimension(measuredWidth, measuredHeight)
  }

  protected abstract fun onMeasureChildren(widthMeasureSpec: Int, heightMeasureSpec: Int)

  protected fun View.autoMeasure() {
    if (isGone) return
    measure(
      this.defaultWidthMeasureSpec(parentView = this@CustomLayout),
      this.defaultHeightMeasureSpec(parentView = this@CustomLayout)
    )
  }

  protected fun View.forEachAutoMeasure() {
    forEach { it.autoMeasure() }
  }

  // todo 适配 margin
  protected fun View.layout(x: Int, y: Int, fromRight: Boolean = false) {
    if (isGone) return
    if (!fromRight) {
      layout(
        x + marginStart,
        y + marginTop,
        x + measuredWidth + marginStart,
        y + measuredHeight + marginTop)
    } else {
      layout(this@CustomLayout.measuredWidth - x - measuredWidth - marginEnd, y)
    }
  }

  protected fun View.defaultWidthMeasureSpec(parentView: ViewGroup): Int {
    return when (layoutParams.width) {
      ViewGroup.LayoutParams.MATCH_PARENT -> parentView.measuredWidth.toExactlyMeasureSpec()
      ViewGroup.LayoutParams.WRAP_CONTENT -> Int.MAX_VALUE.toAtMostMeasureSpec()
      0 -> throw IllegalAccessException("Need special treatment for $this")
      else -> layoutParams.width.toExactlyMeasureSpec()
    }
  }

  protected fun View.defaultHeightMeasureSpec(parentView: ViewGroup): Int {
    return when (layoutParams.height) {
      ViewGroup.LayoutParams.MATCH_PARENT -> parentView.measuredHeight.toExactlyMeasureSpec()
      ViewGroup.LayoutParams.WRAP_CONTENT -> Int.MAX_VALUE.toAtMostMeasureSpec()
      0 -> throw IllegalAccessException("Need special treatment for $this")
      else -> layoutParams.height.toExactlyMeasureSpec()
    }
  }

  protected fun Int.toExactlyMeasureSpec(): Int {
    return MeasureSpec.makeMeasureSpec(this, MeasureSpec.EXACTLY)
  }

  protected fun Int.toAtMostMeasureSpec(): Int {
    return MeasureSpec.makeMeasureSpec(this, MeasureSpec.AT_MOST)
  }

  fun addView(child: View, width: Int, height: Int, apply: (LayoutParams.() -> Unit)) {
    val params = generateDefaultLayoutParams()
    params.width = width
    params.height = height
    params.apply { apply.invoke(this) }
    super.addView(child, params)
  }

  fun View.overScrollNever() {
    overScrollMode = View.OVER_SCROLL_NEVER
  }

  fun ViewGroup.horizontalCenterX(child: View) = (measuredWidth - child.measuredWidth) / 2

  fun ViewGroup.verticalCenterTop(child: View) = (measuredHeight - child.measuredHeight) / 2

  protected val View.measuredWidthWithMargins get() = (measuredWidth + marginLeft + marginRight)

  protected val View.measuredHeightWithMargins get() = (measuredHeight + marginTop + marginBottom)

  // todo 在 View.layout 时，如果需要根据其他 AnchorView 时来定位，为了防止 Anchor.margin 失效
  protected val View.alignLeft get() = left

  protected val View.alignTop get() = top

  protected val View.toRightOf get() = right + marginEnd

  protected val View.below get() = bottom + marginBottom
  // todo END
}
