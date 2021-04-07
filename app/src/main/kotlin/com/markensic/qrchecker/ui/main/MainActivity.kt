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
import com.markensic.sdk.global.sdkLoge
import com.markensic.sdk.utils.FileUtils
import okio.*
import java.io.BufferedInputStream
import java.io.File
import java.nio.charset.Charset
import kotlin.concurrent.thread
import kotlin.io.use

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

  override fun onResume() {
    super.onResume()
    thread {
      val p = Runtime.getRuntime().exec("logcat")
      p.inputStream.source().use { source ->
        source.buffer().use { buffer ->
          var data = buffer.readUtf8Line()
          while (data != null) {
            Log.d("Tesly", data)
            data = buffer.readUtf8Line()
          }
        }
      }
    }
  }
}