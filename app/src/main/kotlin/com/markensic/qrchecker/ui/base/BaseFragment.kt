package com.markensic.qrchecker.ui.base

import androidx.navigation.fragment.NavHostFragment
import com.markensic.qrchecker.viewmodel.SharedViewModel
import org.markensic.mvvm.base.BaseDataBindingFragment

abstract class BaseFragment : BaseDataBindingFragment() {

  protected val sharedViewModel by lazy {
    getAndroidScopeViewModel(SharedViewModel::class)
  }

  protected fun nav() = NavHostFragment.findNavController(this)
}
