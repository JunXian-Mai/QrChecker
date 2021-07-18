package org.markensic.mvvm.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.markensic.sdk.global.sdkLogd
import java.util.concurrent.ConcurrentHashMap

class ProtectedUnPeekLiveData<T> : LiveData<T>() {

    private val observerMap = ConcurrentHashMap<Observer<in T>, ProtectedUnPeekLiveData<T>.ObserverProxy>()
    protected var nullable = false

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val ob = getObserverProxy(observer)
        ob?.let {
            super.observe(owner, observer)
        }
    }

    override fun observeForever(observer: Observer<in T>) {
        val ob = getObserverProxy(observer)
        ob?.let {
            super.observeForever(observer)
        }
    }

    fun stickyObserve(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, observer)
    }

    fun stickyObserveForever(observer: Observer<in T>) {
        super.observeForever(observer)
    }

    override fun setValue(value: T) {
        if (value != null || nullable) {
            var entry: Map.Entry<*, *>
            val iterator = observerMap.entries.iterator()
            while (iterator.hasNext()) {
                entry = iterator.next()
                entry.value.state = true
            }
            super.setValue(value)
        }
    }

    override fun removeObserver(observer: Observer<in T>) {
        var proxy: Observer<in T>?
        var target: Observer<in T>?
        if (observer is ProtectedUnPeekLiveData.ObserverProxy) {
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
            super.removeObserver(observer)
        }
    }

    private fun getObserverProxy(observer: Observer<in T>): Observer<in T>? {
        return if (observerMap.containsKey(observer)) {
            sdkLogd("observe repeatedly, observer has been attached to owner")
            null
        } else {
            ObserverProxy(observer).also {
                observerMap[observer] = it
            }
        }
    }

    private inner class ObserverProxy(
        val target: Observer<in T>,
        var state: Boolean = false
    ) : Observer<T> {

        override fun onChanged(t: T) {
            val proxy = observerMap[target]
            if (proxy?.state == true) {
                proxy.state = false
                if (t != null || nullable) {
                    target.onChanged(t)
                }
            }
        }
    }
}

