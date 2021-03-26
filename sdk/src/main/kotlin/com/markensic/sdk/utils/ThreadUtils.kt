package com.markensic.sdk.utils

import android.os.Looper
import android.widget.Toast
import com.google.common.util.concurrent.ThreadFactoryBuilder
import com.markensic.sdk.framework.thread.ModifyThreadPool
import com.markensic.sdk.global.App
import java.util.concurrent.RejectedExecutionHandler
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object ThreadUtils {
  val tag = this::class.java.simpleName

  val threadPoolMap: MutableMap<String, ModifyThreadPool> = mutableMapOf()

  val cpuCount: Int
    get() {
      return Runtime.getRuntime().availableProcessors()
    }

  fun getThreadFactory(name: String) =
    ThreadFactoryBuilder().setNameFormat("$name-task-%d").build()

  fun doMainThread(action: () -> Unit) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
      action()
    } else {
      App.currentActivity?.runOnUiThread {
        action()
      } ?: run {
        if (App.isDebug) {
          Looper.prepare()
          Toast.makeText(App.sApplication, "doMainThread Error Not Found Activity", Toast.LENGTH_LONG).show()
          Looper.loop()
        }
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