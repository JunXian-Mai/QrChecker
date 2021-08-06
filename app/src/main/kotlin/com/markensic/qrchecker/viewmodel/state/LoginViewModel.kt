package com.markensic.qrchecker.viewmodel.state

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
  val loginBtnText = "Login"

  val account = ObservableField("")

  val password = ObservableField("")

}
