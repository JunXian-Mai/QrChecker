package org.markensic.mvvm.databinding

import android.content.Context
import android.util.AttributeSet
import android.util.SparseArray
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.markensic.core.framework.lazy.LazyImpl
import com.markensic.core.framework.ui.CustomLayout
import com.markensic.core.global.CoreApp
import com.markensic.core.global.log.CoreLog
import org.markensic.mvvm.viewmodel.androidViewModelProvider

abstract class DataBindingLayout @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : CustomLayout(context, attrs, defStyleAttr), LifecycleOwner {

  val androidViewModelProvider: ViewModelProvider by androidViewModelProvider {
    CoreApp.sApplication
  }

  inline fun <reified VM : AndroidViewModel> androidScopeViewModel(): Lazy<VM> {
    return LazyImpl {
      androidViewModelProvider.get(VM::class.java)
    }
  }

  private val variableParams: SparseArray<Any> = SparseArray()

  fun <T> variableId(variableId: Int): Lazy<T> {
    return LazyImpl { findVariableById(variableId) }
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

  fun bindVariableParams(variableId: Int, model: Any) {
    variableParams.put(variableId, model)
  }

  override fun getLifecycle(): Lifecycle {
    return if (context is LifecycleOwner) {
      (context as LifecycleOwner).lifecycle
    } else {
      throw IllegalArgumentException("This context is not a LifecycleOwner!")
    }
  }
}
