package com.markensic.qrchecker.viewmodel

import android.app.Application
import android.content.res.Resources
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AppSharedViewModel(app: Application): AndroidViewModel(app) {
    val packageName = app.packageName

    val versionCode = app.packageManager.getPackageInfo(packageName, 0).versionName
}