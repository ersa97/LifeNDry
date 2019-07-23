package com.lifendry.laundry.lifendry.ui.customer.detail

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lifendry.laundry.lifendry.base.BaseViewModel
import com.lifendry.laundry.lifendry.model.customer.Customer
import com.lifendry.laundry.lifendry.service.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailCustomerViewModel : BaseViewModel() {
    private val customerLiveData: MutableLiveData<Customer> = MutableLiveData()

    fun getCustomerLiveData(): LiveData<Customer> {
        return customerLiveData
    }

    fun setCustomer(customer: Customer) {
        customerLiveData.value = customer
    }

    fun delete(callback: (Int) -> Unit){
        scope.launch {
            setLoading(true)

            val response = withContext(Dispatchers.IO){
                dataSource?.doDeleteCustomer(customerLiveData.value?.id!!)
            }

            when(response){
                is Result.Success -> {
                    when(response.data.errorCode){
                        0 -> callback(0)
                        -1 -> callback(-1)
                    }
                }

                is Result.Error -> {
                    Log.d(DetailCustomerViewModel::class.java.simpleName, response.exception.message)
                    callback(-99)
                }
            }

            setLoading(false)
        }
    }
}