package com.markensic.qrchecker.ui.main

import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.markensic.qrchecker.R
import com.markensic.qrchecker.databinding.ActivityMainBinding
import com.markensic.qrchecker.ui.base.BaseActivity
import com.markensic.qrchecker.ui.custom.MainLayout
import com.markensic.qrchecker.viewmodel.MainViewModel
import com.markensic.sdk.global.App
import com.markensic.sdk.global.AppLog
import com.markensic.sdk.global.sdkLogd
import com.markensic.sdk.utils.FileUtils
import java.io.File

class MainActivity : BaseActivity() {

  private lateinit var binding: ActivityMainBinding
  private val viewModel by viewModels<MainViewModel>()

  override fun bindingView(): View {
    binding = DataBindingUtil.inflate(
      layoutInflater,
      R.layout.activity_main,
      null,
      false
    )
    val mainLayout = MainLayout(this)
    binding.container.addView(mainLayout)
    return binding.root
  }
}