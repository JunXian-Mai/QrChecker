package com.markensic.qrchecker.ui.base

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.markensic.sdk.global.sdkLogd
import com.markensic.sdk.ui.Ui

abstract class BaseActivity : AppCompatActivity() {
  protected abstract fun setContentView(): View

  protected abstract fun hanldeModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Ui.setSystemBar(window)
    setContentView()
    sdkLogd("statusBarSize: ${Ui.statusBarSize}")
    sdkLogd("navigationBarSize : ${Ui.navigationBarSize}")
    hanldeModel()
  }
}