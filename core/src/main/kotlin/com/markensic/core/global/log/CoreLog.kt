package com.markensic.core.global.log

import android.os.Build
import android.os.DeadSystemException
import com.markensic.core.delegate.SpMap
import com.markensic.core.global.App
import com.markensic.core.global.log.Timber.Forest.plant
import com.markensic.core.utils.FileUtils
import com.markensic.core.utils.ThreadUtils
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintWriter
import java.net.UnknownHostException
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

object CoreLog {

  private val logTree = CoreLogTree()

  var forceEnable = false
    set(value) {
      field = value
      if (value && !App.isDebug) {
        initLog()
      }
    }

  var maxLogLevel = LogLevel.ASSERT

  var saveToFile = true

  fun initLog() {
    plant(logTree)
  }

  private fun isEnable(): Boolean {
    Utils.checkLogFileVailTime()
    return forceEnable || App.isDebug
  }

  private fun printlnLog(
    level: LogLevel,
    tag: String?,
    message: String?,
    tr: Throwable?,
    outToLogcat: () -> Unit
  ) {
    isEnable().let {
      val trMsg = handleThrowable(tr)

      val finalTag = tag ?: logTree.tag ?: "Nil"

      val finalMsg = message ?: ""

      val logMsg = if (trMsg.isBlank()) {
        "${level.simpleTag}/$finalTag: $finalMsg"
      } else {
        "${level.simpleTag}/$finalTag: $finalMsg\n$trMsg"
      }

      if (it) outputLog(level, logMsg, outToLogcat)
    }
  }

  private fun handleThrowable(tr: Throwable?): String {
    val trMsg = StringBuilder("")
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
    return trMsg.toString()
  }

  private fun outputLog(level: LogLevel, message: String, outLogcat: () -> Unit) {
    if (maxLogLevel.value >= level.value) {
      if (saveToFile) Utils.writeToFile(message)
      outLogcat.invoke()
    }
  }

  fun v(tag: String, message: String, tr: Throwable? = null) {
    printlnLog(LogLevel.VERBOSE, tag, message, tr) {
      Timber.tag(tag).v(tr, message)
    }
  }

  fun v(message: String, tr: Throwable? = null) {
    printlnLog(LogLevel.VERBOSE, null, message, tr) {
      Timber.v(tr, message)
    }
  }

  fun e(tag: String, message: String, tr: Throwable? = null) {
    printlnLog(LogLevel.ERROR, tag, message, tr) {
      Timber.tag(tag).e(tr, message)
    }
  }

  fun e(message: String, tr: Throwable? = null) {
    printlnLog(LogLevel.ERROR, null, message, tr) {
      Timber.e(tr, message)
    }
  }

  fun e(tr: Throwable) {
    printlnLog(LogLevel.ERROR, null, null, tr) {
      Timber.e(tr)
    }
  }

  fun i(tag: String, message: String, tr: Throwable? = null) {
    printlnLog(LogLevel.INFO, tag, message, tr) {
      Timber.tag(tag).i(tr, message)
    }
  }

  fun i(message: String, tr: Throwable? = null) {
    printlnLog(LogLevel.INFO, null, message, tr) {
      Timber.i(tr, message)
    }
  }

  fun d(tag: String, message: String, tr: Throwable? = null) {
    printlnLog(LogLevel.DEBUG, tag, message, tr) {
      Timber.tag(tag).d(tr, message)
    }
  }

  fun d(message: String, tr: Throwable? = null) {
    printlnLog(LogLevel.DEBUG, null, message, tr) {
      Timber.d(tr, message)
    }
  }

  fun w(tag: String, message: String, tr: Throwable? = null) {
    printlnLog(LogLevel.WARN, tag, message, tr) {
      Timber.tag(tag).w(tr, message)
    }
  }

  fun w(message: String, tr: Throwable? = null) {
    printlnLog(LogLevel.WARN, null, message, tr) {
      Timber.w(tr, message)
    }
  }

  fun setSaveDay(vailDay: Int) {
    Utils.saveDay = vailDay
  }

  fun setLogPath(path: String) {
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
            file.name.substring(0, file.name.lastIndexOf(".log")).let { dateStr ->
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
