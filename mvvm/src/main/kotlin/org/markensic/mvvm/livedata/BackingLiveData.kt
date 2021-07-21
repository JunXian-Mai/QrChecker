package org.markensic.mvvm.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.markensic.core.global.log.CoreLog
import java.util.concurrent.ConcurrentHashMap

open class BackingLiveData<T> : LiveData<T>() {
  private val tag = "v6.1.0-beta1"
  private val observerMap = ConcurrentHashMap<Observer<in T>, ObserverProxy>()
  protected var nullable = false

  override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
    val ob = getObserverProxy(observer)
    ob?.let {
      super.observe(owner, it)
    }
  }

  override fun observeForever(observer: Observer<in T>) {
    val ob = getObserverProxy(observer)
    ob?.let {
      super.observeForever(it)
    }
  }

  fun stickyObserve(owner: LifecycleOwner, observer: Observer<in T>) {
    super.observe(owner, observer)
  }

  fun stickyObserveForever(observer: Observer<in T>) {
    super.observeForever(observer)
  }

  protected override fun setValue(value: T?) {
    if (value != null || nullable) {
      for ((_, proxy) in observerMap.entries) {
        proxy.allowPush = true
      }
      super.setValue(value)
    }
  }

  override fun removeObserver(observer: Observer<in T>) {
    val proxy: Observer<in T>?
    val target: Observer<in T>?
    if (observer is BackingLiveData.ObserverProxy) {
      proxy = observer
      target = observer.target
    } else {
      proxy = observerMap[observer]
      target = if (proxy != null) {
        observer
      } else {
        null
      }
    }

    if (proxy != null && target != null) {
      observerMap.remove(target)
      super.removeObserver(proxy)
    }
  }

  private fun getObserverProxy(observer: Observer<in T>): Observer<in T>? {
    return if (observerMap.containsKey(observer)) {
      CoreLog.d("BackingLiveData $tag -> observe repeatedly, observer has been attached to owner")
      null
    } else {
      ObserverProxy(observer).also {
        observerMap[observer] = it
      }
    }
  }

  open fun clear() {
    super.setValue(null)
  }

  private inner class ObserverProxy(
    val target: Observer<in T>,
    var allowPush: Boolean = false
  ) : Observer<T> {

    override fun onChanged(t: T) {
      val proxy = observerMap[target]

      if (proxy?.allowPush == true) {
        proxy.allowPush = false
        if (t != null || nullable) {
          target.onChanged(t)
        }
      }
    }
  }
}

