package com.lifendry.laundry.lifendry.ui.detailtransaction

import androidx.lifecycle.MutableLiveData
import com.lifendry.laundry.lifendry.base.BaseViewModel
import com.lifendry.laundry.lifendry.model.transaction.Transaction
import com.lifendry.laundry.lifendry.service.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailTransactionViewModel : BaseViewModel() {


    val transactionLiveData: MutableLiveData<Transaction> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun reload(){
        scope.launch {
            setLoading(true)

            val response = withContext(Dispatchers.IO){
                dataSource?.doGetTransaction(transactionLiveData.value?.idTransaction)
            }

            when(response){
                is Result.Success -> {
                    if(response.data.errorCode == 0){
                        transactionLiveData.value = response.data.data
                    }
                }

                is Result.Error -> {
                    errorLiveData.postValue("Terjadi kesalahan, silahkan coba lagi");
                }
            }

            setLoading(false)
        }
    }

    fun paid(callback: (Int) -> Unit) {
        scope.launch {
            setLoading(true)

            val response = withContext(Dispatchers.IO){
                dataSource?.doPaidTransaction(getServerLiveData().value?.idServer, transactionLiveData.value?.idTransaction)
            }

            when(response){
                is Result.Success -> {
                    if(response.data.errorCode == 0){
                        transactionLiveData.value = response.data.data
                    }
                    callback(response.data.errorCode)
                }

                is Result.Error -> {
                    callback(-99)
                }
            }

            setLoading(false)
        }
    }

    fun take(callback: (Int) -> Unit) {

        if(transactionLiveData.value?.done == 0){
            callback(-2)
        }

        scope.launch {
            setLoading(true)

            val response = withContext(Dispatchers.IO){
                dataSource?.doTakeTransaction(getServerLiveData().value?.idServer, transactionLiveData.value?.idTransaction)
            }

            when(response){
                is Result.Success -> {
                    if(response.data.errorCode == 0){
                        transactionLiveData.value = response.data.data
                    }
                    callback(response.data.errorCode)
                }

                is Result.Error -> {
                    callback(-99)
                }
            }

            setLoading(false)
        }
    }
}