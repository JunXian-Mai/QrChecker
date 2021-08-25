package org.markensic.mvvm.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

abstract class BaseMvvmApplication : Application(), ViewModelStoreOwner {
  private lateinit var viewModelStore: ViewModelStore

  override fun attachBaseContext(base: Context?) {
    super.attachBaseContext(base)
    viewModelStore = ViewModelStore()
  }

  override fun getViewModelStore() = viewModelStore
}
