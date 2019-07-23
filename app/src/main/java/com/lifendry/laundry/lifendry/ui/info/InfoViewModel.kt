package com.lifendry.laundry.lifendry.ui.info

import androidx.lifecycle.MutableLiveData
import com.lifendry.laundry.lifendry.base.BaseViewModel

class InfoViewModel : BaseViewModel() {
    val infoLiveData: MutableLiveData<String> = MutableLiveData()
}