package com.markensic.sdk.global

fun <T : Any> T.sdkLogi(log: String) {
  AppLog.i(getTag(this), log)
}

fun <T : Any> T.sdkLoge(log: String) {
  AppLog.e(getTag(this), log)
}

fun <T : Any> T.sdkLogd(log: String) {
  AppLog.d(getTag(this), log)
}

fun <T : Any> T.sdkLogv(log: String) {
  AppLog.v(getTag(this), log)
}

fun <T : Any> T.sdkLogw(log: String) {
  AppLog.w(getTag(this), log)
}

fun <T : String?> T.print() {
  val str = this as String?
  if (str?.isNotBlank() == true) {
    AppLog.i("", str)
  }
}


private fun getTag(any: Any): String {
  var name = any::class.java.name
  if (name.contains("$")) {
    name = name.split("$")[0]
  }
  val simpleName = any::class.java.simpleName
  return if (simpleName.isNotBlank()) {
    simpleName
  } else {
    name
  }
}