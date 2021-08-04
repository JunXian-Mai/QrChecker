package com.markensic.qrchecker.ui.page

import android.content.Context
import android.os.Bundle
import android.util.SparseArray
import android.view.View
import androidx.core.util.set
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
    addVariableParam(BR.click, ClickProxy())
  }

  override fun bindView(context: Context): SparseArray<View>? {
    val spArray = SparseArray<View>()
    spArray[0] = MainLayout(hostActivity!!)
    return spArray
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
