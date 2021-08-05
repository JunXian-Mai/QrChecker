package com.markensic.qrchecker.ui.page

import android.content.Context
import android.os.Bundle
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import com.markensic.qrchecker.R
import com.markensic.qrchecker.ui.base.BaseFragment
import com.markensic.qrchecker.ui.custom.MainLayoutTmp
import com.markensic.qrchecker.viewmodel.ThirdFragmentViewModel
import com.markensic.core.global.log.CoreLog
import org.markensic.mvvm.databinding.DataBindingImpl

class UserFragment : BaseFragment() {

  private val thirdFragmentViewModel by fragmentScopeViewModel<ThirdFragmentViewModel>()

  override fun getDataBindingImpl(): DataBindingImpl = DataBindingImpl(R.layout.fragment_main)

  override fun bindView(context: Context): SparseArray<View>? {
    val spArray = SparseArray<View>()
    spArray.append(
      0,
      MainLayoutTmp(hostActivity!!).apply {
        showTv.text = "ThirdFragment"

        nextEvent.visibility = View.INVISIBLE

        event.setOnClickListener {
          sharedViewModel.changeName("Third")
        }

        eventTv.text = sharedViewModel.name.value

        sharedViewModel.name.observe(this@UserFragment) {
          CoreLog.d("ThirdFragment -> $it")
          eventTv.text = it
        }
      }
    )
    return spArray
  }
}
