package com.markensic.core.framework.lazy

import java.lang.IllegalArgumentException

class LazyImpl<T>(val initializer: () -> T?) : Lazy<T> {

  private val lock = Any()

  @Volatile var _value: T? = null

  override val value: T
    get() {
      if (_value == null){
        synchronized(lock) {
          if (_value == null) {
            _value = initializer()
            if (_value == null) {
              throw IllegalArgumentException("Lazy value initializer failer, please check the lazy method!")
            }
          }
        }
      }
      return _value!!
    }

  override fun isInitialized(): Boolean = _value != null

  override fun toString(): String = if (isInitialized()) _value.toString() else "Lazy value not initialized yet."

}
