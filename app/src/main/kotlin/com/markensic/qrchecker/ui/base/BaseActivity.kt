package com.markensic.qrchecker.ui.base

import com.markensic.qrchecker.viewmodel.SharedViewModel
import org.markensic.mvvm.base.BaseDataBindingActivity

abstract class BaseActivity : BaseDataBindingActivity() {

  protected val sharedViewModel by lazy {
    getAndroidScopeViewModel(SharedViewModel::class)
  }
}
