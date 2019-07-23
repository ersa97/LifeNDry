package com.lifendry.laundry.lifendry.ui.customer.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lifendry.laundry.lifendry.base.BaseViewModel
import com.lifendry.laundry.lifendry.model.customer.Customer
import com.lifendry.laundry.lifendry.service.Result
import com.lifendry.laundry.lifendry.utils.isEmail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FormCustomerViewModel : BaseViewModel() {
    val customerIdLiveData: MutableLiveData<String> = MutableLiveData()
    val customerNameLiveData: MutableLiveData<String> = MutableLiveData()
    val customerEmailLiveData: MutableLiveData<String> = MutableLiveData()
    val customerPhoneLiveData: MutableLiveData<String> = MutableLiveData()
    val customerAddressLiveData: MutableLiveData<String> = MutableLiveData()

    private val customerNameErrorLiveData: MutableLiveData<String> = MutableLiveData()
    private val customerEmailErrorLiveData: MutableLiveData<String> = MutableLiveData()
    private val customerPhoneErrorLiveData: MutableLiveData<String> = MutableLiveData()
    private val customerAddressErrorLiveData: MutableLiveData<String> = MutableLiveData()

    fun getCustomerNameErrorLiveData(): LiveData<String> = customerNameErrorLiveData
    fun getCustomerEmailErrorLiveData(): LiveData<String> = customerEmailErrorLiveData
    fun getCustomerPhoneErrorLiveData(): LiveData<String> = customerPhoneErrorLiveData
    fun getCustomerAddressErrorLiveData(): LiveData<String> = customerAddressErrorLiveData


    fun setCustomer(customer: Customer) {
        customerIdLiveData.value = customer.id
        customerNameLiveData.value = customer.name
        customerEmailLiveData.value = customer.email
        customerAddressLiveData.value = customer.address
        customerPhoneLiveData.value = customer.phoneNumber
    }

    fun submit(callback: (Int) -> Unit) {
        //0 success
        //-98 validate error
        //-1 email is used
        //-99 result error
        if (!validate()) {
            callback(-98)
        } else {
            scope.launch {
                setLoading(true)
                val response = withContext(Dispatchers.IO) {
                    if (customerIdLiveData.value.isNullOrEmpty()) {
                        dataSource?.doCreateCustomer(customerEmailLiveData.value,
                            customerNameLiveData.value,
                            customerPhoneLiveData.value,
                            customerEmailLiveData.value)
                    } else {
                        dataSource?.doEditCustomer(
                            customerIdLiveData.value,
                            customerEmailLiveData.value,
                            customerNameLiveData.value,
                            customerPhoneLiveData.value,
                            customerEmailLiveData.value)
                    }
                }

                when (response) {
                    is Result.Success -> {
                        when (response.data.errorCode) {
                            0 -> callback(0)
                            -1 -> {
                                customerEmailErrorLiveData.value = "Email sudah digunakan, gunakan email yang lain"
                                callback(-1)
                            }

                        }
                    }
                    is Result.Error -> {
                        callback(-99)
                    }
                }

                setLoading(false)
            }
        }

    }

    private fun validate(): Boolean {
        var errorCount = 0
        if (customerNameLiveData.value?.trim().isNullOrEmpty()) {
            customerNameErrorLiveData.value = "Harap isi kolom ini"
            errorCount++
        } else {
            customerNameErrorLiveData.value = null
        }

        if (customerEmailLiveData.value?.trim().isNullOrEmpty()) {
            customerEmailErrorLiveData.value = "Harap isi kolom ini"
            errorCount++
        } else {
            if (customerEmailLiveData.value?.trim()?.isEmail() == true) {
                customerEmailErrorLiveData.value = null
            } else {
                customerEmailErrorLiveData.value = "Email tidak valid"
                errorCount++
            }
        }

        if (customerPhoneLiveData.value?.trim().isNullOrEmpty()) {
            customerPhoneErrorLiveData.value = "Harap isi kolom ini"
            errorCount++
        } else {
            customerPhoneErrorLiveData.value = null
        }

        if (customerAddressLiveData.value?.trim().isNullOrEmpty()) {
            customerAddressErrorLiveData.value = "Harap isi kolom ini"
            errorCount++
        } else {
            customerAddressErrorLiveData.value = null
        }


        return errorCount == 0
    }
}