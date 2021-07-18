package com.markensic.qrchecker.viewmodel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.markensic.sdk.global.App

class MainViewModel : ViewModel() {
    val name = MutableLiveData<String?>("MainViewModel")
    fun ss() {
        name.value = null
    }
}