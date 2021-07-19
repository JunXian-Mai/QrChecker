package com.markensic.qrchecker.ui.page

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.NavHostFragment
import com.markensic.qrchecker.R
import com.markensic.qrchecker.ui.custom.MainLayout
import com.markensic.qrchecker.viewmodel.AppSharedViewModel
import com.markensic.qrchecker.viewmodel.MainFragmentViewModel
import com.markensic.sdk.global.sdkLogd
import org.markensic.mvvm.base.BaseDataBindingFragment
import org.markensic.mvvm.databinding.DataBindingImpl

class MainFragment: BaseDataBindingFragment() {

    private val sharedViewModel by lazy {
        getAndroidScopeViewModel(AppSharedViewModel::class)
    }

    private val mainFragmentViewModel by lazy {
        getFragmentScopeViewModel(MainFragmentViewModel::class)
    }

    override fun getDataBindingImpl(): DataBindingImpl = DataBindingImpl(R.layout.fragment_main)

    override fun onDataBindingCreate(databinding: ViewDataBinding) {
        (databinding.root as ViewGroup).addView(
            MainLayout(hostActivity!!).apply {
                tag = "MainLayout"

                showTv.text = "MainFragment"

                nextEvent.setOnClickListener {
                    NavHostFragment.findNavController(this@MainFragment).navigate(R.id.action_mainFragment_to_secondFragment)
                }

                event.setOnClickListener {
                    sharedViewModel.changeName("Main")
                }

                eventTv.text = sharedViewModel.name.value
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.name.observe(this) {
            sdkLogd("MainFragment -> $it")
            (getDataBinding().root as ViewGroup).findViewWithTag<MainLayout>("MainLayout").apply {
                eventTv.text = it
            }
        }
    }
}