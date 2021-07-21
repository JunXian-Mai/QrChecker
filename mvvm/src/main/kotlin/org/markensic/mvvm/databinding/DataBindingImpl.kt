package org.markensic.mvvm.databinding

import android.util.SparseArray
import androidx.core.util.set
import org.markensic.mvvm.viewmodel.StateViewModelImpl

data class DataBindingImpl(
  val layoutId: Int,
  val stateViewModelImpl: StateViewModelImpl? = null
) {
  val variableParams: SparseArray<Any> = SparseArray()

  fun addVariableParam(variableId: Int, any: Any) {
    variableParams[variableId] = any
  }
}
