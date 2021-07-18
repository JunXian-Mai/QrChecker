package com.markensic.qrchecker.app

import android.content.Context
import com.markensic.sdk.global.ActivityStack
import com.markensic.sdk.global.App
import com.markensic.sdk.global.LibStackContext
import org.markensic.mvvm.base.BaseApplication

class QrCheckerApp : BaseApplication(), LibStackContext {

  override val activityStack: ActivityStack = ActivityStack(true)

  override fun attachBaseContext(base: Context?) {
    super.attachBaseContext(base)
    App.initApplication(this)
  }
}