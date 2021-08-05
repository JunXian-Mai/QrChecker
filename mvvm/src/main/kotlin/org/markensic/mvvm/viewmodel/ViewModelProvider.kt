package org.markensic.mvvm.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.markensic.core.framework.lazy.LazyImpl

fun androidViewModelProvider(activity: () -> Activity): Lazy<ViewModelProvider> {
  return LazyImpl {
    val app = activity().application
    if (app !is ViewModelStoreOwner) {
      throw IllegalStateException("Your application is not yet implements ViewModelStoreOwner.")
    }
    ViewModelProvider.AndroidViewModelFactory.getInstance(app).let {
      ViewModelProvider(app as ViewModelStoreOwner, it)
    }
  }
}

fun normalViewModelProvider(owner: () -> ViewModelStoreOwner): Lazy<ViewModelProvider> {
  return LazyImpl {
    ViewModelProvider(owner())
  }
}
