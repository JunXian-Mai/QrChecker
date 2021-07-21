package com.markensic.core.framework.thread

import java.util.concurrent.RejectedExecutionHandler
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class ModifyThreadPool(
  corePoolSize: Int,
  maximumPoolSize: Int,
  keepAliveTime: Long,
  unit: TimeUnit,
  workQueue: Int,
  threadFactory: ThreadFactory,
  handler: RejectedExecutionHandler,
  private val isSingle: Boolean = false
) : ThreadPoolExecutor(
  corePoolSize,
  maximumPoolSize,
  keepAliveTime,
  unit,
  ResizableCapacityLinkedBlockingQueue<Runnable>(workQueue),
  threadFactory,
  handler
) {

  init {
    //允许回收核心线程，实现动态修改线程池
    allowCoreThreadTimeOut(true)
  }

  override fun execute(command: Runnable?) {
    if (queue is ResizableCapacityLinkedBlockingQueue) {
      (queue as ResizableCapacityLinkedBlockingQueue<Runnable>).also { queue ->
        if (queue.remainingCapacity() - corePoolSize <= 0) {
          val allSize = queue.size + queue.remainingCapacity()
          if (!isSingle) {
            corePoolSize *= 2
            maximumPoolSize = corePoolSize
            prestartAllCoreThreads()
            queue.setCapacity(allSize * maximumPoolSize)
          } else {
            queue.setCapacity(allSize * 2)
          }
        }
      }
    }
    super.execute(command)
  }
}
