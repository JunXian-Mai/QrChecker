package com.markensic.qrchecker.ui.page

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.markensic.qrchecker.R
import com.markensic.qrchecker.ui.base.BaseFragment
import com.markensic.qrchecker.ui.custom.MainLayout
import com.markensic.qrchecker.viewmodel.MainFragmentViewModel
import com.markensic.sdk.global.log.CoreLog
import org.markensic.mvvm.databinding.DataBindingImpl

class MainFragment : BaseFragment() {

  private val mainFragmentViewModel by lazy {
    getFragmentScopeViewModel(MainFragmentViewModel::class)
  }

  override fun getDataBindingImpl(): DataBindingImpl = DataBindingImpl(R.layout.fragment_main)

  private fun navSecondPage() {
    nav().navigate(R.id.action_mainFragment_to_secondFragment)
  }

  override fun onDataBindingCreate(databinding: ViewDataBinding) {
    (databinding.root as ViewGroup).addView(
      MainLayout(hostActivity!!).apply {
        tag = "MainLayout"


        loginIv.setOnClickListener {
          navSecondPage()
        }
      }
    )
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    sharedViewModel.name.observe(this) {
      CoreLog.d("MainFragment -> $it")

    }
  }
}
