package com.markensic.qrchecker.ui.main

import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.markensic.qrchecker.R
import com.markensic.qrchecker.databinding.ActivityMainBinding
import com.markensic.qrchecker.ui.base.BaseActivity
import com.markensic.qrchecker.ui.custom.MainLayout
import com.markensic.qrchecker.viewmodel.MainViewModel
import com.markensic.sdk.global.sdkLogd
import kotlinx.coroutines.*

class MainActivity : BaseActivity() {

  private lateinit var binding: ActivityMainBinding
  private val viewModel by viewModels<MainViewModel>()

  override fun setContentView(): View {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    val mainLayout = MainLayout(this)
    binding.container.addView(mainLayout)
    return binding.root
  }

  override fun hanldeModel() {
  }
}