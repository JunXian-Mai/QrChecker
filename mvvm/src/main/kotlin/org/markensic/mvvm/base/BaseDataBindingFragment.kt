package org.markensic.mvvm.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.markensic.core.framework.lazy.LazyImpl
import org.markensic.mvvm.databinding.DataBindingImpl
import org.markensic.mvvm.databinding.DataBindingLayout
import org.markensic.mvvm.viewmodel.androidViewModelProvider
import org.markensic.mvvm.viewmodel.normalViewModelProvider

abstract class BaseDataBindingFragment : Fragment() {

  protected abstract fun getDataBindingImpl(): DataBindingImpl

  private var databinding: ViewDataBinding? = null

  protected var hostActivity: AppCompatActivity? = null

  val androidViewModelProvider: ViewModelProvider by androidViewModelProvider {
    hostActivity?.application
      ?: throw IllegalStateException("Fragment be detached, can't create ViewModelProvider")
  }

  val activityViewModelProvider: ViewModelProvider by normalViewModelProvider {
    hostActivity
      ?: throw IllegalStateException("Fragment be detached, can't create ViewModelProvider")
  }

  val fragmentViewModelProvider by normalViewModelProvider { this }

  inline fun <reified VM : AndroidViewModel> androidScopeViewModel(): Lazy<VM> {
    return LazyImpl {
      androidViewModelProvider.get(VM::class.java)
    }
  }

  inline fun <reified VM : ViewModel> activityScopeViewModel(): Lazy<VM> {
    return LazyImpl {
      activityViewModelProvider.get(VM::class.java)
    }
  }

  inline fun <reified VM : ViewModel> fragmentScopeViewModel(): Lazy<VM> {
    return LazyImpl {
      fragmentViewModelProvider.get(VM::class.java)
    }
  }

  override fun onAttach(context: Context) {
    super.onAttach(context)
    hostActivity = context as AppCompatActivity
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val dataBindingImpl = getDataBindingImpl()

    val binding: ViewDataBinding = DataBindingUtil.inflate(inflater, dataBindingImpl.layoutId, container, false)

    binding.apply {
      lifecycleOwner = this@BaseDataBindingFragment

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

    return databinding?.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    databinding?.unbind()
    databinding = null
  }

}
