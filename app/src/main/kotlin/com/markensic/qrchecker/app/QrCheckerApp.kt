package com.markensic.qrchecker.app

import android.app.Application
import com.markensic.sdk.global.ActivityStack
import com.markensic.sdk.global.App
import com.markensic.sdk.global.LibStackContext

class QrCheckerApp : LibStackContext, Application() {

  private val stackContext = ActivityStack()

  override fun onCreate() {
    super.onCreate()
    App.initApplication(this)
    App.initStackContext(this)
  }

  override fun getActivityStack(): ActivityStack {
    return stackContext
  }
}