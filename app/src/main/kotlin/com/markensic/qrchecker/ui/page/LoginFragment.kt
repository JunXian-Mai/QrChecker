package com.markensic.qrchecker.ui.page

import android.os.Bundle
import android.view.View
import com.markensic.core.global.log.CoreLog
import com.markensic.qrchecker.BR
import com.markensic.qrchecker.R
import com.markensic.qrchecker.ui.base.BaseFragment
import com.markensic.qrchecker.viewmodel.state.LoginViewModel
import org.markensic.mvvm.databinding.DataBindingImpl

class LoginFragment : BaseFragment() {

  private val loginState by fragmentScopeViewModel<LoginViewModel>()

  override fun getDataBindingImpl() = DataBindingImpl(R.layout.fragment_login).apply {
    addVariableParam(BR.vm, loginState)
    addVariableParam(BR.click, ClickProxy())
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    sharedViewModel.name.observe(viewLifecycleOwner) {
      CoreLog.d("LoginFragment -> $it")
    }
  }

  inner class ClickProxy {

    fun login(v: View) {
      CoreLog.d(
        """
          accout: ${loginState.account.get()}
          password: ${loginState.password.get()}
        """.trimIndent()
      )
      nav().navigate(R.id.action_loginFragment_to_userFragment)
    }
  }
}
