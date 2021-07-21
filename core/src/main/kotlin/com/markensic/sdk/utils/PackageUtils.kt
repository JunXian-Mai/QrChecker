package com.markensic.sdk.utils

import com.markensic.sdk.global.App

object PackageUtils {
  val packageName = App.sApplication.packageName

  val versionName = App.sApplication.packageManager.getPackageInfo(packageName, 0).versionName
}
