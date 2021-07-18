package org.markensic.mvvm.base

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.forEach
import androidx.core.view.marginEnd
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.markensic.sdk.global.App
import com.markensic.sdk.ui.Ui
import com.markensic.sdk.ui.dp
import com.markensic.sdk.ui.sp
import com.markensic.sdk.utils.PackageUtils
import org.markensic.mvvm.R
import org.markensic.mvvm.databinding.DataBindingImpl
import kotlin.reflect.KClass

abstract class BaseDataBindingActivity : AppCompatActivity() {

    private val androidViewModelProvider: ViewModelProvider by lazy {
        val app = this.application
        if (app !is ViewModelStoreOwner) {
            throw IllegalStateException(
                "Your application is not yet implements ViewModelStoreOwner."
            )
        }
        ViewModelProvider.AndroidViewModelFactory.getInstance(app).let {
            ViewModelProvider(app as ViewModelStoreOwner, it)
        }
    }
    private val activityViewModelProvider: ViewModelProvider by lazy {
        ViewModelProvider(this)
    }

    protected fun <VM : AndroidViewModel> getAndroidScopeViewModel(kClass: KClass<VM>) =
        androidViewModelProvider.get(kClass.java)

    protected fun <VM : ViewModel> getActivityScopeViewModel(kClass: KClass<VM>) =
        activityViewModelProvider.get(kClass.java)

    protected abstract fun getDataBindingImpl(): DataBindingImpl

    protected open fun onDataBindingCreate(databinding: ViewDataBinding) {}

    protected open fun isCustomImmersion() = false

    private var databinding: ViewDataBinding? = null
    private lateinit var versionTextView: TextView

    protected fun getDataBinding(): ViewDataBinding =
        databinding ?: throw NullPointerException("DataBinding not yet initialize or be destroyed")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isCustomImmersion()) {
            Ui.setSystemBar(window)
        }

        val dataBindingImpl = getDataBindingImpl()

        val binding: ViewDataBinding = DataBindingUtil.setContentView(this, dataBindingImpl.layoutId)
        binding.lifecycleOwner = this
        dataBindingImpl.stateViewModelImpl?.let {
            binding.setVariable(it.stateVariableId, it.stateViewModel)
        }
        dataBindingImpl.variableParams.forEach { key, value ->
            binding.setVariable(key, value)
        }

        onDataBindingCreate(binding)

        if (App.isDebug) {
            addDebugVersionTip()
        }

        databinding = binding
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