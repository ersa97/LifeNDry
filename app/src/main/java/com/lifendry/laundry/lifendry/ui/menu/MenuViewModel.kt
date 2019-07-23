package com.lifendry.laundry.lifendry.ui.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lifendry.laundry.lifendry.base.BaseViewModel
import com.lifendry.laundry.lifendry.model.menu.Menu
import com.lifendry.laundry.lifendry.service.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MenuViewModel : BaseViewModel() {
    private val menusLiveData: MutableLiveData<List<Menu>> = MutableLiveData()
    private val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun getMenusLiveData(): LiveData<List<Menu>> = menusLiveData

    init {
        loadMenu()
    }

    private fun loadMenu() {
        scope.launch {
            setLoading(true)
            val response = withContext(Dispatchers.IO) {
                dataSource?.doShowMenu()
            }

            when (response) {
                is Result.Success -> {
                    if (response.data.errorCode == 0) {
                        menusLiveData.value = response.data.data
                    }
                }

                is Result.Error -> {
                    errorLiveData.value = "Terjadi error pada saat mencari pelanggan"
                }
            }

            setLoading(false)

        }
    }
}