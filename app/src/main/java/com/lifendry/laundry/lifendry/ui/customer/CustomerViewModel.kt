package com.lifendry.laundry.lifendry.ui.customer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lifendry.laundry.lifendry.base.BaseViewModel
import com.lifendry.laundry.lifendry.model.customer.Customer
import com.lifendry.laundry.lifendry.service.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CustomerViewModel : BaseViewModel() {
    private val customersLiveData: MutableLiveData<List<Customer>> = MutableLiveData()
    private val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun getCustomersLiveData(): LiveData<List<Customer>> = customersLiveData

    fun searchCustomer(text: String) {
        scope.launch {
            setLoading(true)
            val response = withContext(Dispatchers.IO) {
                dataSource?.doSearchCustomer(text)
            }

            when (response) {
                is Result.Success -> {
                    if (response.data.errorCode == 0) {
                        customersLiveData.value = response.data.data
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