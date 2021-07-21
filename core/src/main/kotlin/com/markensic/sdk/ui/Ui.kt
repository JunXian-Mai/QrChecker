package com.markensic.sdk.ui

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.view.*
import com.markensic.sdk.global.App

object Ui {

  val displayWidth = Display.realWidth

  val displayHeight = Display.realHeight

  private const val NOT_MEASURED = -1

  @Volatile
  private var _statusBarSize = NOT_MEASURED

  val statusBarSize: Int
    get() {
      if (_statusBarSize == NOT_MEASURED) {
        synchronized(Ui::class) {
          if (_statusBarSize == NOT_MEASURED) {
            _statusBarSize = Resources.getSystem().let { res ->
              res.getIdentifier(
                "status_bar_height",
                "dimen",
                "android"
              ).let {
                res.getDimensionPixelSize(it)
              }
            }
          }
        }
      }
      return _statusBarSize
    }

  @Volatile
  private var _navigationBarSize = NOT_MEASURED

  val navigationBarSize: Int
    get() {
      if (_navigationBarSize == NOT_MEASURED) {
        synchronized(Ui::class) {
          if (_navigationBarSize == NOT_MEASURED) {
            val navigationBarSystemSize = Resources.getSystem().let { res ->
              res.getIdentifier(
                "navigation_bar_height",
                "dimen",
                "android"
              ).let {
                res.getDimensionPixelSize(it)
              }
            }
            (App.sApplication.getSystemService(Context.WINDOW_SERVICE) as WindowManager).apply {
              val windowFullHeight = Display.realHeight
              val windowHeight = defaultDisplay.height
              var remainEffectHeight = windowFullHeight - windowHeight
              if (remainEffectHeight > navigationBarSystemSize) {
                remainEffectHeight -= statusBarSize
              }

              _navigationBarSize = if (remainEffectHeight - statusBarSize <= 0) {
                // navigationBar hide
                0
              } else {
                remainEffectHeight.coerceAtLeast(navigationBarSystemSize)
              }
            }
          }
        }
      }
      return _navigationBarSize
    }

  fun reMeasureNavigationBarSize(window: Window, run: (() -> Unit)? = null) {
    //分屏, 键盘弹出适配
    window.decorView.let {
      it.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
          val r = Rect()
          val rootViewHeight = it.rootView.height
          if (rootViewHeight <= 0) return

          it.getWindowVisibleDisplayFrame(r)
          val height: Int = rootViewHeight - r.bottom

          if (height >= 0) {
            _navigationBarSize = height
          }
          it.viewTreeObserver.removeOnGlobalLayoutListener(this)
          run?.invoke()
        }
      })
    }
  }

  fun setSystemBar(
    window: Window,
    immersionStatusBar: Boolean = true,
    immersionNavigationBar: Boolean = true,
    statusBarColor: Int = Color.TRANSPARENT,
    navigationBarColor: Int = Color.TRANSPARENT,
    lightModel: Boolean = false
  ) {
    window.apply {
      decorView.apply {
        systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        if (immersionStatusBar) {
          systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (immersionNavigationBar) {
          systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
        if (lightModel) {
          systemUiVisibility = systemUiVisibility or
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
            View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
      }

      addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
      if (immersionStatusBar) {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        this.statusBarColor = statusBarColor
      }
      if (immersionNavigationBar) {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
          //去除Android Q 中导航栏的半透明罩
          isNavigationBarContrastEnforced = false
        }
        this.navigationBarColor = navigationBarColor
      }
    }
  }

  fun setSystemBarColor(
    window: Window,
    statusBarColor: Int = Color.TRANSPARENT,
    navigationBarColor: Int = Color.TRANSPARENT
  ) {
    window.apply {
      addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
      clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
      this.statusBarColor = statusBarColor
      clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        //去除Android Q 中导航栏的半透明罩
        isNavigationBarContrastEnforced = false
      }
      this.navigationBarColor = navigationBarColor
    }
  }
}
