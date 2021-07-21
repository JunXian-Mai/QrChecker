package org.markensic.mvvm.databinding

import android.view.View
import androidx.databinding.BindingAdapter
import com.markensic.core.framework.event.OnDebouncingClickProxy

@BindingAdapter("mvvm:onDebouncingClick")
fun onDebouncingClick(view: View, listener: (View) -> Unit) {
  view.setOnClickListener(OnDebouncingClickProxy(listener))
}
