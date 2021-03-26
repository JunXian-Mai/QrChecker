package com.markensic.sdk.framework.thread

import com.markensic.sdk.framework.reflect.rejectFinalModifier
import java.lang.reflect.Field
import java.util.concurrent.LinkedBlockingQueue

class ResizableCapacityLinkedBlockIngQueue<E>(capacity: Int) : LinkedBlockingQueue<E>(capacity) {
  val field: Field? by lazy {
    this::class.java.superclass.rejectFinalModifier("capacity")
  }

  fun setCapacity(capacity: Int) {
    field?.set(this, capacity)
  }

  fun getCapacity(): Int = (field?.get(this) ?: 0) as Int
}