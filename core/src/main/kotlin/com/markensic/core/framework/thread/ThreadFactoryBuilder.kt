package com.markensic.core.framework.thread

import java.lang.NullPointerException
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicLong

class ThreadFactoryBuilder {
  private var nameFormat: String? = null
  private var daemon: Boolean? = null
  private var priority: Int? = null
  private var uncaughtExceptionHandler: Thread.UncaughtExceptionHandler? = null
  private var backingThreadFactory: ThreadFactory? = null
  fun setNameFormat(nameFormat: String): ThreadFactoryBuilder {
    format(nameFormat, 0)
    this.nameFormat = nameFormat
    return this
  }

  fun setDaemon(daemon: Boolean): ThreadFactoryBuilder {
    this.daemon = daemon
    return this
  }

  fun setPriority(priority: Int): ThreadFactoryBuilder {
    checkArgument(priority >= 1, "Thread priority (%s) must be >= %s", priority, 1)
    checkArgument(priority <= 10, "Thread priority (%s) must be <= %s", priority, 10)
    this.priority = priority
    return this
  }

  fun setUncaughtExceptionHandler(uncaughtExceptionHandler: Thread.UncaughtExceptionHandler): ThreadFactoryBuilder {
    this.uncaughtExceptionHandler = checkNotNull(uncaughtExceptionHandler)
    return this
  }

  fun setThreadFactory(backingThreadFactory: ThreadFactory): ThreadFactoryBuilder {
    this.backingThreadFactory = checkNotNull(backingThreadFactory)
    return this
  }

  fun build(): ThreadFactory {
    return doBuild(this)
  }

  private fun checkArgument(b: Boolean, errorMessageTemplate: String, vararg p: Any?) {
    if (!b) {
      throw IllegalArgumentException(String.format(errorMessageTemplate, p))
    }
  }

  private fun <T> checkNotNull(reference: T) = reference ?: throw NullPointerException()

  companion object {
    private fun doBuild(builder: ThreadFactoryBuilder): ThreadFactory {
      val nameFormat = builder.nameFormat
      val daemon = builder.daemon
      val priority = builder.priority
      val uncaughtExceptionHandler = builder.uncaughtExceptionHandler
      val backingThreadFactory =
        if (builder.backingThreadFactory != null) builder.backingThreadFactory else Executors.defaultThreadFactory()
      val count = if (nameFormat != null) AtomicLong(0L) else null
      return ThreadFactory { runnable ->
        val thread = backingThreadFactory!!.newThread(runnable)
        if (nameFormat != null) {
          thread.name = format(nameFormat, count!!.getAndIncrement())
        }
        if (daemon != null) {
          thread.isDaemon = daemon
        }
        if (priority != null) {
          thread.priority = priority
        }
        if (uncaughtExceptionHandler != null) {
          thread.uncaughtExceptionHandler = uncaughtExceptionHandler
        }
        thread
      }
    }

    private fun format(format: String, vararg args: Any): String {
      return String.format(Locale.ROOT, format, *args)
    }
  }
}
