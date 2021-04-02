package com.markensic.qrchecker.app

import android.app.Application
import android.content.Context
import com.markensic.sdk.global.ActivityStack
import com.markensic.sdk.global.App
import com.markensic.sdk.global.LibStackContext

class QrCheckerApp : Application(), LibStackContext {

  override val activityStack: ActivityStack = ActivityStack(true)

  override fun attachBaseContext(base: Context?) {
    super.attachBaseContext(base)
    App.initApplication(this)
  }

  override fun onCreate() {
    super.onCreate()
  }
}