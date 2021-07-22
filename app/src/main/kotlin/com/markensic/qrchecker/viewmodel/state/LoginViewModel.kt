package com.markensic.qrchecker.viewmodel.state

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.markensic.core.global.log.CoreLog

class LoginViewModel : ViewModel() {
  val loginBtnText = "Login"

  val account = ObservableField("")

  val password = ObservableField("")

}