package org.markensic.mvvm.viewmodel

import androidx.lifecycle.ViewModel

data class StateViewModelImpl(
  val stateVariableId: Int,
  val stateViewModel: ViewModel
)
