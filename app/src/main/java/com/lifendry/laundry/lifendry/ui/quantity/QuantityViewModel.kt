package com.lifendry.laundry.lifendry.ui.quantity

import androidx.lifecycle.MutableLiveData
import com.lifendry.laundry.lifendry.base.BaseViewModel

class QuantityViewModel : BaseViewModel(){
    var quantityLiveData: MutableLiveData<Double> = MutableLiveData()

}