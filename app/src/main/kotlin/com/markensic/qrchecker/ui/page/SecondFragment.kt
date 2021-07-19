package com.markensic.qrchecker.ui.page

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.NavHostFragment
import com.markensic.qrchecker.R
import com.markensic.qrchecker.ui.custom.MainLayout
import com.markensic.qrchecker.viewmodel.AppSharedViewModel
import com.markensic.qrchecker.viewmodel.SecondFragmentViewModel
import com.markensic.sdk.global.sdkLogd
import org.markensic.mvvm.base.BaseDataBindingFragment
import org.markensic.mvvm.databinding.DataBindingImpl

class SecondFragment: BaseDataBindingFragment() {

    private val sharedViewModel by lazy {
        getAndroidScopeViewModel(AppSharedViewModel::class)
    }

    private val secondFragment by lazy {
        getFragmentScopeViewModel(SecondFragmentViewModel::class)
    }

    override fun getDataBindingImpl(): DataBindingImpl = DataBindingImpl(R.layout.fragment_main)

    override fun onDataBindingCreate(databinding: ViewDataBinding) {
        (databinding.root as ViewGroup).addView(
            MainLayout(hostActivity!!).apply {
                tag = "MainLayout"

                showTv.text = "SecondFragment"

                nextEvent.setOnClickListener {
                    NavHostFragment.findNavController(this@SecondFragment).navigate(R.id.action_secondFragment_to_thirdFragment)
                }

                event.setOnClickListener {
                    sharedViewModel.changeName("Second")
                }

                eventTv.text = sharedViewModel.name.value
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.name.observe(this) {
            sdkLogd("SecondFragment -> $it")
            (getDataBinding().root as ViewGroup).findViewWithTag<MainLayout>("MainLayout").apply {
                eventTv.text = it
            }
        }
    }
}