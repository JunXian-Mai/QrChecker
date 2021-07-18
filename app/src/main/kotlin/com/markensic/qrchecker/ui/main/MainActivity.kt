package com.markensic.qrchecker.ui.main

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.markensic.qrchecker.BR
import com.markensic.qrchecker.R
import com.markensic.qrchecker.databinding.ActivityMainBinding
import com.markensic.qrchecker.ui.custom.MainLayout
import com.markensic.qrchecker.viewmodel.AppSharedViewModel
import com.markensic.qrchecker.viewmodel.MainViewModel
import org.markensic.mvvm.base.BaseDataBindingActivity
import org.markensic.mvvm.databinding.DataBindingImpl
import org.markensic.mvvm.databinding.StateViewModelImpl

class MainActivity : BaseDataBindingActivity() {

  private val sharedViewModel by lazy {
    getAndroidScopeViewModel(AppSharedViewModel::class)
  }

  private val activityViewModel by lazy {
    getActivityScopeViewModel(MainViewModel::class)
  }

  override fun onDataBindingCreate(databinding: ViewDataBinding) {
    (databinding.root as ViewGroup).addView(MainLayout(this))
  }


  override fun getDataBindingImpl(): DataBindingImpl =
    DataBindingImpl(R.layout.activity_main, StateViewModelImpl(BR.vm, activityViewModel))
}