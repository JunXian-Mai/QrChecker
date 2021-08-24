package com.markensic.core.utils

import android.os.Looper
import com.markensic.core.framework.thread.ModifyThreadPool
import com.markensic.core.framework.thread.ThreadFactoryBuilder
import com.markensic.core.global.App
import java.util.concurrent.RejectedExecutionHandler
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object ThreadUtils {

  private val threadPoolMap: MutableMap<String, ModifyThreadPool> = mutableMapOf()

  private val cpuCount: Int
    get() {
      return Runtime.getRuntime().availableProcessors()
    }

  private fun getThreadFactory(name: String) =
    ThreadFactoryBuilder().setNameFormat("$name-task-%d").build()

  fun doMainThread(action: () -> Unit) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
      action()
    } else {
      App.currentActivity.runOnUiThread {
        action()
      }
    }
  }

  fun createVariableThreadPool(
    poolName: String,
    keepLive: Pair<Long, TimeUnit> = 60.toLong() to TimeUnit.SECONDS,
    reject: RejectedExecutionHandler = ThreadPoolExecutor.AbortPolicy()
  ): ModifyThreadPool {
    val coreSize = cpuCount
    val maxSize = cpuCount * 2
    val queueCount = coreSize * maxSize

    return threadPoolMap[poolName].let { pool ->
      pool ?: ModifyThreadPool(
        coreSize,
        maxSize,
        keepLive.first,
        keepLive.second,
        queueCount,
        getThreadFactory(poolName),
        reject
      ).also {
        threadPoolMap[poolName] = it
      }
    }
  }

  fun createSingleThreadPool(
    poolName: String,
    keepLive: Pair<Long, TimeUnit> = 60.toLong() to TimeUnit.SECONDS,
    reject: RejectedExecutionHandler = ThreadPoolExecutor.AbortPolicy()
  ): ModifyThreadPool {
    val coreSize = 1
    val maxSize = 1
    val queueCount = coreSize * 10

    return threadPoolMap[poolName].let { pool ->
      pool ?: ModifyThreadPool(
        coreSize,
        maxSize,
        keepLive.first,
        keepLive.second,
        queueCount,
        getThreadFactory(poolName),
        reject,
        true
      ).also {
        threadPoolMap[poolName] = it
      }
    }
  }
}
