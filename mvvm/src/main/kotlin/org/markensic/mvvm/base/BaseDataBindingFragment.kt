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
import androidx.lifecycle.ViewModelStoreOwner
import org.markensic.mvvm.databinding.DataBindingImpl
import kotlin.reflect.KClass

abstract class BaseDataBindingFragment : Fragment() {

    private var databinding: ViewDataBinding? = null
    private var hostActivity: AppCompatActivity? = null

    private val androidViewModelProvider: ViewModelProvider by lazy {
        val app = hostActivity?.application
            ?: throw IllegalStateException("Fragment be detached, can't create ViewModelProvider")
        if (app !is ViewModelStoreOwner) {
            throw IllegalStateException("Your application is not yet implements ViewModelStoreOwner.")
        }
        ViewModelProvider.AndroidViewModelFactory.getInstance(app).let {
            ViewModelProvider(app as ViewModelStoreOwner, it)
        }
    }
    private val activityViewModelProvider: ViewModelProvider by lazy {
        hostActivity?.let {
            ViewModelProvider(it)
        } ?: throw IllegalStateException("Fragment be detached, can't create ViewModelProvider")
    }
    private val fragmentViewModelProvider: ViewModelProvider by lazy {
        ViewModelProvider(this)
    }

    protected fun <VM : AndroidViewModel> getAndroidScopeViewModel(kClass: KClass<VM>) =
        androidViewModelProvider.get(kClass.java)

    protected fun <VM : ViewModel> getActivityScopeViewModel(kClass: KClass<VM>) =
        activityViewModelProvider.get(kClass.java)

    protected fun <VM : ViewModel> getFragmentScopeViewModel(kClass: KClass<VM>) =
        fragmentViewModelProvider.get(kClass.java)

    protected abstract fun getDataBindingImpl(): DataBindingImpl

    protected open fun onDataBindingCreate(databinding: ViewDataBinding) {}

    protected fun getDataBinding(): ViewDataBinding =
        databinding ?: throw NullPointerException("DataBinding not yet initialize or be destroyed")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        hostActivity = context as AppCompatActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dataBindingImpl = getDataBindingImpl()

        val binding: ViewDataBinding = DataBindingUtil.inflate(inflater, dataBindingImpl.layoutId, container, false)
        binding.lifecycleOwner = this
        dataBindingImpl.stateViewModelImpl?.let {
            binding.setVariable(it.stateVariableId, it.stateViewModel)
        }
        dataBindingImpl.variableParams.forEach { key, value ->
            binding.setVariable(key, value)
        }

        onDataBindingCreate(binding)

        databinding = binding

        return databinding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        databinding?.unbind()
        databinding = null
    }

}