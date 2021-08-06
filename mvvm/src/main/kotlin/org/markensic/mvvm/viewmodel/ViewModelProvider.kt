package org.markensic.mvvm.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.markensic.core.framework.lazy.LazyImpl

fun androidViewModelProvider(app: () -> Application): Lazy<ViewModelProvider> {
  return LazyImpl {
    val application = app()
    if (application !is ViewModelStoreOwner) {
      throw IllegalStateException("Your application is not yet implements ViewModelStoreOwner.")
    }
    ViewModelProvider.AndroidViewModelFactory.getInstance(application).let {
      ViewModelProvider(application as ViewModelStoreOwner, it)
    }
  }
}

fun normalViewModelProvider(owner: () -> ViewModelStoreOwner): Lazy<ViewModelProvider> {
  return LazyImpl {
    ViewModelProvider(owner())
  }
}
