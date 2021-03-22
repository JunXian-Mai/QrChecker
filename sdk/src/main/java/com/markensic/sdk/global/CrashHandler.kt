package com.markensic.sdk.global

import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import com.markensic.sdk.ui.Display
import com.markensic.sdk.utils.FileUtils
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

object CrashHandler : Thread.UncaughtExceptionHandler {

  private val path =
    App.sApplication.getExternalFilesDir(null)!!.absolutePath + File.separator + "crashfolder" + File.separator

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

  fun collectDeviceInfo(): String {
    return StringBuilder().also { builder ->
      App.sApplication.packageManager?.also { pm ->
        val packInfo = pm.getPackageInfo(App.sApplication.packageName, PackageManager.GET_ACTIVITIES)
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
          VersionCode: $code ${"\n\n"}
        """.trimIndent()
        )

        builder.append("Hardware Information:\n")
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
          builder.append(
            """
              ${it.name}: ${it.get(null)}${"\n"}
            """.trimIndent()
          )
        }
      }
    }.toString()
  }

  private fun handleException(t: Thread, tr: Throwable): Boolean {
    return tr.let { ex ->
      val fileName =
        "Crash_${SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒").format(Date())}_${tr::class.java.simpleName}.log"
      FileUtils.createFile(path + fileName).also { file ->
        file.printWriter().use { writer ->
          writer.println(
            """
              Crash Time:
              ${SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(Date())}${"\n"}
            """.trimIndent()
          )
          writer.println(collectDeviceInfo())
          writer.println(
            """
              Thread: ${t.name}
              Exception StackTrace:
            """.trimIndent()
          )
          ex.forEarch { cause ->
            cause.printStackTrace(writer)
          }
        }
        upLoadCrashListener?.upLoadCrashFile(file)
      }
      upLoadCrashListener?.upLoadThrowable(ex)
      true
    }
  }

  private fun Throwable.forEarch(function: (cause: Throwable) -> Unit) {
    this.also {
      function(it)
    }.cause?.forEarch(function)
  }

  interface UploadListener {
    fun upLoadCrashFile(f: File)

    fun upLoadThrowable(tr: Throwable)
  }
}