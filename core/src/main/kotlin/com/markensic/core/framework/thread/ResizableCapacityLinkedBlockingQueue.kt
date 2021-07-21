package com.markensic.core.framework.thread

import com.markensic.core.framework.reflect.getAndRemoveFinalModifier
import java.lang.reflect.Field
import java.util.concurrent.LinkedBlockingQueue

class ResizableCapacityLinkedBlockingQueue<E>(capacity: Int) : LinkedBlockingQueue<E>(capacity) {
  private val field: Field by lazy {
    this::class.java.superclass.getAndRemoveFinalModifier("capacity")
  }

  fun setCapacity(capacity: Int) {
    field.set(this, capacity)
  }

  fun getCapacity(): Int = (field.get(this) ?: 0) as Int
}
