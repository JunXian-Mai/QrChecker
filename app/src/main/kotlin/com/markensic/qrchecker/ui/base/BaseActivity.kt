package com.markensic.qrchecker.ui.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.markensic.sdk.global.sdkLogd
import com.markensic.sdk.ui.Ui

abstract class BaseActivity : AppCompatActivity() {
  protected abstract fun bindingView(): View

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Ui.setSystemBar(window)
    setContentView(bindingView())
    sdkLogd("statusBarSize: ${Ui.statusBarSize}")
    sdkLogd("navigationBarSize : ${Ui.navigationBarSize}")
  }
}