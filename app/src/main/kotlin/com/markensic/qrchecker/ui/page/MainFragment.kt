package com.markensic.qrchecker.ui.page

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.markensic.qrchecker.R
import com.markensic.qrchecker.ui.custom.MainLayout
import org.markensic.mvvm.base.BaseDataBindingFragment
import org.markensic.mvvm.databinding.DataBindingImpl

class MainFragment: BaseDataBindingFragment() {

    override fun getDataBindingImpl(): DataBindingImpl = DataBindingImpl(R.layout.fragment_main)

    override fun onDataBindingCreate(databinding: ViewDataBinding) {
        (databinding.root as ViewGroup).addView(MainLayout(hostActivity!!))
    }
}