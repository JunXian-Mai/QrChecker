package com.markensic.qrchecker.ui.page

import com.markensic.qrchecker.ui.base.BaseFragment
import com.markensic.qrchecker.viewmodel.UserFragmentViewModel
import com.markensic.qrchecker.R
import com.markensic.qrchecker.BR
import org.markensic.mvvm.databinding.DataBindingImpl
import org.markensic.mvvm.viewmodel.StateViewModelImpl

class UserFragment : BaseFragment() {

  private val userFragmentViewModel by fragmentScopeViewModel<UserFragmentViewModel>()

  override fun getDataBindingImpl() = DataBindingImpl(
    R.layout.fragment_user, StateViewModelImpl(BR.vm, userFragmentViewModel)
  )

//  MainLayoutTmp(hostActivity!!).apply {
//    showTv.text = "UserFragment"
//
//    nextEvent.visibility = View.INVISIBLE
//
//    event.setOnClickListener {
//      sharedViewModel.changeName("Third")
//    }
//
//    eventTv.text = sharedViewModel.name.value
//
//    sharedViewModel.name.observe(this@UserFragment) {
//      CoreLog.d("UserFragment -> $it")
//      eventTv.text = it
//    }
//  }
}
