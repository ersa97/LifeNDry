package com.lifendry.laundry.lifendry.ui.menu.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lifendry.laundry.lifendry.base.BaseViewModel
import com.lifendry.laundry.lifendry.model.menu.Menu

class DetailMenuViewModel : BaseViewModel(){
    private val menuLiveData: MutableLiveData<Menu> = MutableLiveData()
    fun getMenuLiveData(): LiveData<Menu> = menuLiveData
    fun setMenuLiveData(menu: Menu){
        menuLiveData.postValue(menu)
    }
}