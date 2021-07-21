package org.markensic.mvvm.databinding

import android.util.SparseArray
import androidx.core.util.set
import androidx.lifecycle.ViewModel

data class DataBindingImpl(
  val layoutId: Int,
  val stateViewModelImpl: StateViewModelImpl? = null
) {
  val variableParams: SparseArray<Any> = SparseArray()

  fun addVariableParam(variableId: Int, any: Any) {
    variableParams[variableId] = any
  }
}

data class StateViewModelImpl(
  val stateVariableId: Int,
  val stateViewModel: ViewModel
)
