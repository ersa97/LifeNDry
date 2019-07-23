package com.lifendry.laundry.lifendry.ui.worker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lifendry.laundry.lifendry.base.BaseViewModel
import com.lifendry.laundry.lifendry.model.user.User
import com.lifendry.laundry.lifendry.model.worker.Worker
import com.lifendry.laundry.lifendry.service.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WorkerViewModel : BaseViewModel() {
    private val userLiveData: MutableLiveData<User> = MutableLiveData()
    private val workersLiveData: MutableLiveData<List<Worker>> = MutableLiveData()
    private val errorLiveData: MutableLiveData<String> = MutableLiveData()


    fun getWorkersLiveData(): LiveData<List<Worker>> = workersLiveData
    fun getErrorLiveData(): LiveData<String> = errorLiveData

    fun setUser(user: User?) {
        userLiveData.value = user
    }

    fun searchWorker(text: String) {
        scope.launch {
            setLoading(true)
            val response = withContext(Dispatchers.IO) {
                userLiveData.value?.id?.let { dataSource?.doSearchWorker(text, it) }
            }

            when (response) {
                is Result.Success -> {
                    if (response.data.errorCode == 0) {
                        workersLiveData.value = response.data.data
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