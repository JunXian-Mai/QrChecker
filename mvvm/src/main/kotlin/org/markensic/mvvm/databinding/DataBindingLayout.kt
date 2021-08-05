package org.markensic.mvvm.databinding

import android.content.Context
import android.util.AttributeSet
import android.util.SparseArray
import androidx.core.util.putAll
import com.markensic.core.framework.lazy.LazyImpl
import com.markensic.core.framework.ui.CustomLayout
import com.markensic.core.global.log.CoreLog

abstract class DataBindingLayout @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : CustomLayout(context, attrs, defStyleAttr) {

  init {
    this.tag = tag ?: this::class.simpleName
  }

  private val variableParams: SparseArray<Any> = SparseArray()

  fun <T> variableId(variableId: Int): Lazy<T> {
    return LazyImpl{ findVariableById(variableId) }
  }

  private fun <T> findVariableById(variableId: Int): T? {
    try {
      return if (variableParams.size() <= 0) {
        CoreLog.e(ArrayIndexOutOfBoundsException("variableParams don't store variable"))
        null
      } else {
        variableParams[variableId] as T
      }
    } catch (e: Exception) {
      throw ClassCastException("VariableId: $variableId don't cast to matching class")
    }
  }

  fun bindVariableParams(params: SparseArray<Any>) {
    variableParams.clear()
    variableParams.putAll(params)
  }
}
