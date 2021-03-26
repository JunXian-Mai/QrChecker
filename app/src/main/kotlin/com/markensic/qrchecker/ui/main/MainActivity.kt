package com.markensic.qrchecker.ui.main

import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.markensic.qrchecker.R
import com.markensic.qrchecker.databinding.ActivityMainBinding
import com.markensic.qrchecker.ui.base.BaseActivity
import com.markensic.qrchecker.ui.base.dp
import com.markensic.qrchecker.ui.custom.MainLayout
import com.markensic.qrchecker.viewmodel.MainViewModel
import java.lang.NullPointerException

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