package com.lifendry.laundry.lifendry.ui.unfinishedtransaction

import androidx.lifecycle.MutableLiveData
import com.lifendry.laundry.lifendry.base.BaseViewModel
import com.lifendry.laundry.lifendry.model.transaction.Transaction
import com.lifendry.laundry.lifendry.model.user.User
import com.lifendry.laundry.lifendry.service.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UnfinishedTransactionViewModel : BaseViewModel() {
    val userLiveData: MutableLiveData<User> = MutableLiveData()
    val transactionsLiveData: MutableLiveData<List<Transaction>> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun setUser(user: User?){
        if(userLiveData.value == null){
            userLiveData.value = user
            load()
        }
    }

    fun load(){
        scope.launch {
            setLoading(true)

            val response = withContext(Dispatchers.IO){
                dataSource?.doShowUnfinishedTransaction(getServerLiveData().value?.idServer, userLiveData.value?.id)
            }

            when(response){
                is Result.Success -> {
                    transactionsLiveData.value = response.data.data
                }
                is Result.Error -> {
                    errorLiveData.value = "Terjadi error pada saat memuat data"
                }
            }
            setLoading(false)
        }
    }
}