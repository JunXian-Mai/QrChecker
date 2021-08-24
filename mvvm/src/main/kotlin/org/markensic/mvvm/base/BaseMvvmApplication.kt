package org.markensic.mvvm.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.markensic.core.global.stack.LibStackContext

abstract class BaseMvvmApplication : Application(), ViewModelStoreOwner, LibStackContext {
  private lateinit var viewModelStore: ViewModelStore

  override fun attachBaseContext(base: Context?) {
    super.attachBaseContext(base)
    viewModelStore = ViewModelStore()
  }

  override fun getViewModelStore() = viewModelStore
}
