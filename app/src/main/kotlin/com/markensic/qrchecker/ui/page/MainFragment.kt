package com.markensic.qrchecker.ui.page

import android.content.Context
import android.os.Bundle
import android.util.SparseArray
import android.view.View
import com.markensic.qrchecker.BR
import com.markensic.qrchecker.R
import com.markensic.qrchecker.ui.base.BaseFragment
import com.markensic.qrchecker.ui.custom.MainLayout
import com.markensic.qrchecker.viewmodel.MainFragmentViewModel
import com.markensic.core.global.log.CoreLog
import org.markensic.mvvm.databinding.DataBindingImpl

class MainFragment : BaseFragment() {

  private val mainFragmentViewModel by lazy {
    getFragmentScopeViewModel(MainFragmentViewModel::class)
  }

  override fun getDataBindingImpl(): DataBindingImpl = DataBindingImpl(R.layout.fragment_main).apply {
    addVariableParam(BR.vm, mainFragmentViewModel)
  }

  private fun navSecondPage() {
    nav().navigate(R.id.action_mainFragment_to_loginFragment)
  }

  override fun bindView(context: Context): SparseArray<View>? {
    val spArray = SparseArray<View>()
    spArray.append(
      0,
      MainLayout(hostActivity!!).apply {
        loginIv.setOnClickListener {
          navSecondPage()
        }
      }
    )
    return spArray
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    sharedViewModel.name.observe(this) {
      CoreLog.d("MainFragment -> $it")
    }
  }
}
