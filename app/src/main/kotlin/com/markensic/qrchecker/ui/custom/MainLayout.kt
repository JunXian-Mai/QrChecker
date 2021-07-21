package com.markensic.qrchecker.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.updateMargins
import com.markensic.qrchecker.R
import com.markensic.core.framework.ui.Display
import com.markensic.core.framework.ui.Ui
import com.markensic.core.framework.ui.dp
import com.markensic.core.framework.ui.matchParent
import org.markensic.mvvm.databinding.DataBindingLayout

class MainLayout @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : DataBindingLayout(context, attrs, defStyleAttr) {

  init {
    this.tag = tag ?: MainLayout::class.simpleName
  }

  val contentHeight = Display.realHeight - Ui.statusBarSize - Ui.navigationBarSize

  val backgroundView = View(context).apply {
    background = ResourcesCompat.getDrawable(context.resources, R.drawable.background, null)
    addView(this, matchParent, matchParent)
  }

  val loginIv = ImageView(context).apply {
    setImageResource(R.drawable.ic_user)
    scaleType = ImageView.ScaleType.FIT_XY
    addView(this, 35.dp, 35.dp) {
      updateMargins(top = 15.dp, right = 15.dp)
    }
  }

  val qrScaneIv = ImageView(context).apply {
    setImageResource(R.drawable.scan_qr)
    scaleType = ImageView.ScaleType.FIT_CENTER
    addView(this, 223.dp, 179.dp)
  }

  override fun onMeasureChildren(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    forEachAutoMeasure()
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    backgroundView.layout(0, 0)
    loginIv.layout(0, Ui.statusBarSize, true)
    qrScaneIv.layout(
      horizontalCenterX(qrScaneIv),
      Ui.statusBarSize + (contentHeight - qrScaneIv.measuredHeight) / 2
    )
  }
}
