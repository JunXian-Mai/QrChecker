package com.markensic.qrchecker.ui

import android.os.Bundle
import androidx.navigation.Navigation
import com.markensic.core.global.log.CoreLog
import com.markensic.qrchecker.BR
import com.markensic.qrchecker.R
import com.markensic.qrchecker.ui.base.BaseActivity
import com.markensic.qrchecker.viewmodel.MainViewModel
import org.markensic.mvvm.databinding.DataBindingImpl
import org.markensic.mvvm.viewmodel.StateViewModelImpl

class MainActivity : BaseActivity() {

  private val activityViewModel by activityScopeViewModel<MainViewModel>()

  override fun getDataBindingImpl() =
    DataBindingImpl(R.layout.activity_main, StateViewModelImpl(BR.vm, activityViewModel)).apply {
      addVariableParam(BR.vm, activityViewModel)
    }

  fun nav() = Navigation.findNavController(this, R.id.main_fragment_host)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    sharedViewModel.name.observe(this) {
      CoreLog.d("MainActivity -> $it")
    }
  }
}
