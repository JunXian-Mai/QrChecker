package com.markensic.core.utils

import com.markensic.core.global.App

object PackageUtils {
  val packageName = App.sApplication.packageName

  val versionName = App.sApplication.packageManager.getPackageInfo(packageName, 0).versionName
}
