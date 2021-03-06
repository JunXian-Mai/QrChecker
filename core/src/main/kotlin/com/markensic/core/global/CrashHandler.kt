package com.markensic.core.global

import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import android.util.Log
import com.markensic.core.framework.ui.Display
import com.markensic.core.utils.FileUtils
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

object CrashHandler : Thread.UncaughtExceptionHandler {

  private val path =
    CoreApp.sApplication.getExternalFilesDir(null)!!.absolutePath + File.separator + "crashFolder" + File.separator

  private val systemHandler = Thread.getDefaultUncaughtExceptionHandler()

  var upLoadCrashListener: UploadListener? = null

  fun init() {
    Thread.setDefaultUncaughtExceptionHandler(this)
  }

  override fun uncaughtException(t: Thread, e: Throwable) {
    handleException(t, e).also {
      if (it) {
        Thread.sleep(3000)
        Process.killProcess(Process.myPid())
        exitProcess(1)
      } else {
        systemHandler?.uncaughtException(t, e)
      }
    }
  }

  private fun collectDeviceInfo(): String {
    return StringBuilder().also { builder ->
      CoreApp.sApplication.packageManager?.also { pm ->
        try {
          val packInfo = pm.getPackageInfo(CoreApp.sApplication.packageName, PackageManager.GET_ACTIVITIES)
          val code: Int by lazy {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
              packInfo.longVersionCode.toInt()
            } else {
              packInfo.versionCode
            }
          }

          builder.append(
            """
              Package Information:
              SDK: ${Build.VERSION.SDK_INT}
              VersionName: ${packInfo.versionName}
              VersionCode: $code${"\n\n"}
            """.trimIndent()
          )

          builder.append(
            "Hardware Information:\n"
          )
          builder.append(
            """
              DISPLAY: ${
              Display.physicsDm.toString().let {
                it.substring(it.indexOf("{") + 1, it.lastIndexOf("}"))
              }
            }${"\n"}
            """.trimIndent()
          )

          Build::class.java.declaredFields.forEach {
            it.isAccessible = true
            var value = ""
            if (it.type == Array<String>::class.java) {
              (it.get(null) as Array<*>).forEach { item ->
                value += "$item ,"
              }
              if (value.isNotBlank()) {
                value = value.substring(0, value.length - 2)
              }
            } else {
              value = it.get(null)?.toString() ?: ""
            }

            builder.append(
              """
                ${it.name}: $value${"\n"}
              """.trimIndent()
            )
          }
        } catch (e: Exception) {
          // eat exception
        }
      }
    }.toString()
  }

  private fun handleException(t: Thread, tr: Throwable): Boolean {
    return tr.let { ex ->
      Log.e("CrashHandler", "catch crash: ${tr::class.java.simpleName}")
      val fileName =
        "Crash_${System.currentTimeMillis()}_${tr::class.java.simpleName}.log"

      FileUtils.createFile(path + fileName)?.also { file ->
        Log.e("CrashHandler", "The detailed information see ${file.absolutePath}")
        file.printWriter().use { writer ->
          writer.println(
            """
              Phone Crash System Time:
              ${SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(Date())}${"\n"}
            """.trimIndent()
          )
          writer.println(
            collectDeviceInfo()
          )
          writer.println(
            """
              Thread: ${t.name}
              Exception StackTrace:
            """.trimIndent()
          )
          ex.forEach { cause ->
            cause.printStackTrace(writer)
          }
        }
        upLoadCrashListener?.upLoadCrashFile(file)
      }
      upLoadCrashListener?.upLoadThrowable(ex)
      true
    }
  }

  private fun Throwable.forEach(function: (cause: Throwable) -> Unit) {
    this.also {
      function(it)
    }.cause?.forEach(function)
  }

  interface UploadListener {
    fun upLoadCrashFile(f: File)

    fun upLoadThrowable(tr: Throwable)
  }
}
