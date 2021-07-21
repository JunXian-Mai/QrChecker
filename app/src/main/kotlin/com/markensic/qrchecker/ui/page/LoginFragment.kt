package com.markensic.qrchecker.ui.page

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.markensic.qrchecker.R
import com.markensic.qrchecker.ui.base.BaseFragment
import com.markensic.qrchecker.ui.custom.MainLayoutTmp
import com.markensic.qrchecker.viewmodel.state.LoginViewModel
import com.markensic.sdk.global.log.CoreLog
import org.markensic.mvvm.databinding.DataBindingImpl

class LoginFragment : BaseFragment() {

  private val loginState by lazy {
    getFragmentScopeViewModel(LoginViewModel::class)
  }

  override fun getDataBindingImpl(): DataBindingImpl = DataBindingImpl(R.layout.fragment_main)

  private fun navThirdPage() {
    nav().navigate(R.id.action_secondFragment_to_thirdFragment)
  }

  override fun onDataBindingCreate(databinding: ViewDataBinding) {
    (databinding.root as ViewGroup).addView(
      MainLayoutTmp(hostActivity!!).apply {
        tag = "MainLayout"

        showTv.text = "SecondFragment"

        nextEvent.setOnClickListener {
          navThirdPage()
        }

        event.setOnClickListener {
          sharedViewModel.changeName("Second")
        }

        eventTv.text = sharedViewModel.name.value
      }
    )
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    sharedViewModel.name.observe(this) {
      CoreLog.d("SecondFragment -> $it")
      (getDataBinding().root as ViewGroup).findViewWithTag<MainLayoutTmp>("MainLayout").apply {
        eventTv.text = it
      }
    }
  }
}
