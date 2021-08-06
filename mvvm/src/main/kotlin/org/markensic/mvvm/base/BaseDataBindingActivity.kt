package org.markensic.mvvm.base

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.markensic.core.framework.lazy.LazyImpl
import com.markensic.core.framework.ui.Ui
import com.markensic.core.framework.ui.dp
import com.markensic.core.framework.ui.sp
import com.markensic.core.global.App
import com.markensic.core.utils.PackageUtils
import org.markensic.mvvm.R
import org.markensic.mvvm.databinding.DataBindingImpl
import org.markensic.mvvm.databinding.DataBindingLayout
import org.markensic.mvvm.viewmodel.androidViewModelProvider
import org.markensic.mvvm.viewmodel.normalViewModelProvider

abstract class BaseDataBindingActivity : AppCompatActivity() {

  protected abstract fun getDataBindingImpl(): DataBindingImpl

  protected open fun customImmersion() = false

  private var databinding: ViewDataBinding? = null

  private lateinit var versionTextView: TextView

  private lateinit var viewRoot: View

  val androidViewModelProvider by androidViewModelProvider { this.application }
  inline fun <reified VM : AndroidViewModel> androidScopeViewModel(): Lazy<VM> {
    return LazyImpl {
      androidViewModelProvider.get(VM::class.java)
    }
  }

  val activityViewModelProvider by normalViewModelProvider { this }
  inline fun <reified VM : ViewModel> activityScopeViewModel(): Lazy<VM> {
    return LazyImpl {
      activityViewModelProvider.get(VM::class.java)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (!customImmersion()) {
      Ui.setSystemBar(window)
    }

    val dataBindingImpl = getDataBindingImpl()

    val binding: ViewDataBinding = DataBindingUtil.setContentView(this, dataBindingImpl.layoutId)

    binding.apply {
      lifecycleOwner = this@BaseDataBindingActivity

      dataBindingImpl.stateViewModelImpl?.let {
        setVariable(it.stateVariableId, it.stateViewModel)
        if (binding.root is DataBindingLayout) {
          (binding.root as DataBindingLayout).bindVariableParams(it.stateVariableId, it.stateViewModel)
        }
      }

      dataBindingImpl.variableParams.forEach { key, value ->
        setVariable(key, value)
        if (binding.root is DataBindingLayout) {
          (binding.root as DataBindingLayout).bindVariableParams(key, value)
        }
      }
    }

    databinding = binding

    if (App.isDebug) {
      addDebugVersionTip()
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    databinding?.unbind()
    databinding = null
  }

  private fun addDebugVersionTip() {
    versionTextView = TextView(App.sApplication).apply {
      alpha = 0.9f
      textSize = 5.sp
      setBackgroundColor(Color.TRANSPARENT)
      typeface = Typeface.defaultFromStyle(Typeface.ITALIC)
      setTextColor(App.sApplication.resources.getColor(R.color.snow_white))
      text = String.format(
        App.sApplication.resources.getString(R.string.debug_tip),
        PackageUtils.versionName
      )
      setPadding(0, 0, 25.dp.toInt(), 0)
      textAlignment = TextView.TEXT_ALIGNMENT_VIEW_END
      gravity = Gravity.BOTTOM
      isClickable = false
      isEnabled = false
      isFocusable = false
    }
    window.addContentView(
      versionTextView,
      ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
      )
    )
  }
}
