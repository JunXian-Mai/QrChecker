package com.markensic.qrchecker.ui.page

import android.os.Bundle
import android.view.View
import com.markensic.qrchecker.BR
import com.markensic.qrchecker.R
import com.markensic.qrchecker.ui.base.BaseFragment
import com.markensic.qrchecker.viewmodel.state.LoginViewModel
import org.markensic.mvvm.databinding.DataBindingImpl

class LoginFragment : BaseFragment() {

  private val loginState by lazy {
    getFragmentScopeViewModel(LoginViewModel::class)
  }

  override fun getDataBindingImpl(): DataBindingImpl = DataBindingImpl(R.layout.fragment_login).apply {
    addVariableParam(BR.vm, loginState)
  }

  private fun navLoginIn() {
    nav().navigate(R.id.action_loginFragment_to_userFragment)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

  }
}
