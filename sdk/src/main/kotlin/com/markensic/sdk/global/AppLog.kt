package com.markensic.sdk.global

import android.os.Build
import android.os.DeadSystemException
import android.util.Log
import com.markensic.sdk.delegate.SpMap
import com.markensic.sdk.utils.FileUtils
import com.markensic.sdk.utils.ThreadUtils
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintWriter
import java.net.UnknownHostException
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

object AppLog {

  var forceEnable = false

  var forceDisEnable = false

  var outputLevel = LogLevel.ASSERT

  var saveToFile = true

  private fun isEnable(): Boolean {
    Utils.checkLogFileVailTime()
    return !forceDisEnable && forceEnable || App.isDebug
  }

  private fun printlnLog(
    level: LogLevel,
    tag: String,
    message: String,
    tr: Throwable?,
    outToLogcat: () -> Int
  ): Int {
    return isEnable().let {
      val trMsg = StringBuilder()
      tr?.forEarch { ex ->
        when (ex) {
          is UnknownHostException -> return@forEarch
          else -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && ex is DeadSystemException) {
              trMsg.append("DeadSystemException: The system died; earlier logs will point to the root cause")
              return@forEarch
            }

            ByteArrayOutputStream().use { baos ->
              PrintWriter(baos).use { write ->
                ex.printStackTrace(write)
              }
              trMsg.append(baos.toString())
            }
          }
        }
      }

      val logMsg = trMsg.let { trStr ->
        if (trStr.toString().isEmpty()) {
          "${level.simpleTag}/$tag: $message"
        } else {
          "${level.simpleTag}/$tag: $message\n$trStr"
        }
      }

      if (it) {
        meetOutputLevel(level, logMsg, outToLogcat)
      } else {
        -1
      }
    }
  }

  private fun meetOutputLevel(level: LogLevel, message: String, outToLogcat: () -> Int): Int {
    return outputLevel.let {
      if (it.value >= level.value) {
        saveToFile(message, outToLogcat)
      } else {
        -1
      }
    }
  }

  private fun saveToFile(message: String, outToLogcat: () -> Int): Int {
    return if (saveToFile) {
      Utils.writeToFile(message)
      outToLogcat()
    } else {
      outToLogcat()
    }
  }


  fun v(tag: String, message: String, tr: Throwable? = null): Int {
    return printlnLog(LogLevel.VERBOSE, tag, message, tr) {
      tr?.let {
        Log.v(tag, message, tr)
      } ?: Log.v(tag, message)
    }
  }

  fun e(tag: String, message: String, tr: Throwable? = null): Int {
    return printlnLog(LogLevel.ERROR, tag, message, tr) {
      tr?.let {
        Log.e(tag, message, tr)
      } ?: Log.e(tag, message)
    }
  }

  fun i(tag: String, message: String, tr: Throwable? = null): Int {
    return printlnLog(LogLevel.INFO, tag, message, tr) {
      tr?.let {
        Log.i(tag, message, tr)
      } ?: Log.i(tag, message)
    }
  }

  fun d(tag: String, message: String, tr: Throwable? = null): Int {
    return printlnLog(LogLevel.DEBUG, tag, message, tr) {
      tr?.let {
        Log.d(tag, message, tr)
      } ?: Log.d(tag, message)
    }
  }

  fun w(tag: String, message: String, tr: Throwable? = null): Int {
    return printlnLog(LogLevel.WARN, tag, message, tr) {
      tr?.let {
        Log.w(tag, message, tr)
      } ?: Log.w(tag, message)
    }
  }

  fun setSaveDay(vailDay: Int) {
    Utils.saveDay = vailDay
  }

  fun setLogFilePath(path: String) {
    Utils.logPath = path
  }

  private fun Throwable.forEarch(function: (cause: Throwable) -> Unit) {
    this.also {
      function(it)
    }.cause?.forEarch(function)
  }

  enum class LogLevel(val value: Int, val simpleTag: String) {
    VERBOSE(2, "V"),
    DEBUG(3, "D"),
    INFO(4, "I"),
    WARN(5, "W"),
    ERROR(6, "E"),
    ASSERT(7, "A");
  }

  private object Utils {
    private const val LOG = "_mSicLog"
    private const val LOG_PATH_KEY = "${LOG}_File_Path"
    private const val LOG_VAIL_KEY = "${LOG}_File_VAIL"
    private const val POOL_NAME = "logThreadPool"

    private val sp = SpMap(LOG)
    private val pool = ThreadUtils.createSingleThreadPool(POOL_NAME)
    private val checkValid = AtomicBoolean(false)

    private val LOG_DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
    private val today: String
      get() {
        return LOG_DATE_FORMAT.format(Date())
      }
    private val logFileName get() = "$today.log"

    private val msgPrefix: String
      get() {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA).format(Date())
      }

    var logPath: String
      get() {
        return sp[LOG_PATH_KEY].let {
          if (it is String) {
            it
          } else {
            App.sApplication.getExternalFilesDir(null)!!.absolutePath + File.separator + "logFolder" + File.separator
          }
        }
      }
      set(value) {
        sp[LOG_PATH_KEY] = value
      }

    var saveDay: Int
      get() {
        return sp[LOG_VAIL_KEY].let {
          if (it is Int) {
            it
          } else {
            30
          }
        }
      }
      set(value) {
        sp[LOG_VAIL_KEY] = value
      }

    fun checkLogFileVailTime() {
      if (checkValid.compareAndSet(false, true)) {
        FileUtils.iterateFileInDir(logPath) { file ->
          if (file.isFile && file.name.endsWith(".log")) {
            val logDateTime = file.name.substring(0, file.name.lastIndexOf(".log")).let { dateStr ->
              LOG_DATE_FORMAT.parse(
                dateStr,
                ParsePosition(0)
              )?.time?.also {
                if ((Date().time - it) / (24 * 60 * 60 * 1000) > saveDay) {
                  file.delete()
                }
              }
            }
          }
        }
      }
    }

    fun writeToFile(msg: String) {
      pool.execute {
        val logFilePath = "$logPath/$logFileName"
        val logText = "$msgPrefix $msg \n"
        FileUtils.appendToFile(logFilePath, logText)
      }
    }
  }
}