package org.markensic.mvvm.databinding

import android.view.View
import androidx.databinding.BindingAdapter
import com.markensic.core.framework.event.applyOnDebouncingClickProxy

@BindingAdapter("android:onDebouncingClick")
fun onDebouncingClick(view: View, listener: View.OnClickListener) {
  view.applyOnDebouncingClickProxy {
    listener.onClick(it)
  }
}
