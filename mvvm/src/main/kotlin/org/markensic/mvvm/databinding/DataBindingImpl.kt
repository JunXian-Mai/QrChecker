package org.markensic.mvvm.databinding

import android.util.SparseArray
import androidx.lifecycle.ViewModel

data class DataBindingImpl(
    val layoutId: Int,
    val stateVariableId: Int,
    val stateViewModel: ViewModel,
    val variableParams: SparseArray<Any> = SparseArray()
)