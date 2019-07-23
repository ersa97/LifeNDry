package com.lifendry.laundry.lifendry.ui.newtransaction

import androidx.lifecycle.MutableLiveData
import com.lifendry.laundry.lifendry.base.BaseViewModel
import com.lifendry.laundry.lifendry.model.customer.Customer
import com.lifendry.laundry.lifendry.model.menu.Menu
import com.lifendry.laundry.lifendry.model.transaction.Transaction
import com.lifendry.laundry.lifendry.model.user.User
import com.lifendry.laundry.lifendry.service.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewTransactionViewModel : BaseViewModel() {
    val userLiveData: MutableLiveData<User> = MutableLiveData()
    val customerLiveData: MutableLiveData<Customer> = MutableLiveData()
    val menuLiveData: MutableLiveData<Menu> = MutableLiveData()
    val quantityLiveData: MutableLiveData<Double> = MutableLiveData()
    val infoLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        quantityLiveData.value = 0.0
        infoLiveData.value = "-"
    }

    fun submit(callback: (Int) -> Unit, success: (Transaction) -> Unit) {
        if (!validate()) {
            callback(-98)
        } else {
            scope.launch {
                setLoading(true)
                val transaction = withContext(Dispatchers.IO) {
                    dataSource?.doCreateTransaction(
                        userLiveData.value?.id,
                        customerLiveData.value?.id,
                        menuLiveData.value?.id,
                        quantityLiveData.value,
                        infoLiveData.value
                    )
                }

                when (transaction) {
                    is Result.Success -> {
                        transaction.data.data?.let { success(it) }
                    }
                    is Result.Error -> {
                        callback(-99)
                    }
                }

                setLoading(false)
            }
        }
    }

    fun validate(): Boolean {
        var errorCount = 0
        if (customerLiveData.value == null) {
            errorCount++
        }

        if (menuLiveData.value == null) {
            errorCount++
        }

        if (quantityLiveData.value == 0.0 || quantityLiveData.value == null) {
            errorCount++
        }

        if (infoLiveData.value.isNullOrEmpty()) {
            errorCount++
        }

        return errorCount == 0
    }
}