package com.markensic.qrchecker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import org.markensic.mvvm.livedata.BackingLiveData
import org.markensic.mvvm.livedata.UnPeekLiveData

class AppSharedViewModel(app: Application) : AndroidViewModel(app) {
    private val _name = UnPeekLiveData<String>("before")

    val name: BackingLiveData<String>
        get() = _name


    fun changeName(n: String) {
        _name.value = "after -> $n"
    }

}