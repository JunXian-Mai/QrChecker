package com.markensic.core.utils

import com.markensic.core.global.CoreApp

object PackageUtils {
  val packageName = CoreApp.sApplication.packageName

  val versionName = CoreApp.sApplication.packageManager.getPackageInfo(packageName, 0).versionName
}
