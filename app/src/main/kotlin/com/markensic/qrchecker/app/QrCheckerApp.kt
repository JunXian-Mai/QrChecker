package com.markensic.qrchecker.app

import android.content.Context
import com.markensic.core.global.App
import com.markensic.core.global.stack.ActivityStack
import com.markensic.core.global.stack.LibStackContext
import org.markensic.mvvm.base.BaseApplication

class QrCheckerApp : BaseApplication(), LibStackContext {

  override val activityStack: ActivityStack = ActivityStack(true)

  override fun attachBaseContext(base: Context?) {
    super.attachBaseContext(base)
    App.initApplication(this)
  }
}
