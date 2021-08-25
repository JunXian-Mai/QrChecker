package com.markensic.qrchecker.app

import android.content.Context
import com.markensic.core.global.CoreApp
import org.markensic.mvvm.base.BaseMvvmApplication

class QrCheckerApp : BaseMvvmApplication() {

  override fun attachBaseContext(base: Context?) {
    super.attachBaseContext(base)
    CoreApp.initCore(this)
  }
}
