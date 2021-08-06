package com.markensic.qrchecker.ui.page

import android.os.Bundle
import android.view.View
import com.markensic.core.global.log.CoreLog
import com.markensic.qrchecker.BR
import com.markensic.qrchecker.R
import com.markensic.qrchecker.ui.base.BaseFragment
import com.markensic.qrchecker.viewmodel.MainFragmentViewModel
import org.markensic.mvvm.databinding.DataBindingImpl
import org.markensic.mvvm.viewmodel.StateViewModelImpl

class MainFragment : BaseFragment() {

  private val mainFragmentViewModel by fragmentScopeViewModel<MainFragmentViewModel>()

  override fun getDataBindingImpl() =
    DataBindingImpl(R.layout.fragment_main, StateViewModelImpl(BR.vm, mainFragmentViewModel)).apply {
      addVariableParam(BR.click, ClickProxy())
    }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    sharedViewModel.name.observe(this) {
      CoreLog.d("MainFragment -> $it")
    }
  }

  inner class ClickProxy {
    fun toLogin() {
      sharedViewModel.changeName("to Login")
      nav().navigate(R.id.action_mainFragment_to_loginFragment)
    }
  }
}
