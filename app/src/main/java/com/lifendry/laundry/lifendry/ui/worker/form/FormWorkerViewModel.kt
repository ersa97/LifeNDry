package com.lifendry.laundry.lifendry.ui.worker.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lifendry.laundry.lifendry.base.BaseViewModel
import com.lifendry.laundry.lifendry.model.user.User
import com.lifendry.laundry.lifendry.model.worker.Worker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.lifendry.laundry.lifendry.service.Result

class FormWorkerViewModel : BaseViewModel() {
    val workerIdLiveData: MutableLiveData<String> = MutableLiveData()
    val workerNameLiveData: MutableLiveData<String> = MutableLiveData()
    val workerPhoneLiveData: MutableLiveData<String> = MutableLiveData()
    val workerAddressLiveData: MutableLiveData<String> = MutableLiveData()
    val workerIsActiveLiveData: MutableLiveData<Int> = MutableLiveData()
    val userLiveData: MutableLiveData<User> = MutableLiveData()

    private val workerNameErrorLiveData: MutableLiveData<String> = MutableLiveData()
    private val workerPhoneErrorLiveData: MutableLiveData<String> = MutableLiveData()
    private val workerAddressErrorLiveData: MutableLiveData<String> = MutableLiveData()

    fun getWorkerNameErrorLiveData(): LiveData<String> = workerNameErrorLiveData
    fun getWorkerPhoneErrorLiveData(): LiveData<String> = workerPhoneErrorLiveData
    fun getWorkerAddressErrorLiveData(): LiveData<String> = workerAddressErrorLiveData

    fun setWorker(worker: Worker) {
        workerIdLiveData.value = worker.idWorker
        workerNameLiveData.value = worker.name
        workerAddressLiveData.value = worker.address
        workerPhoneLiveData.value = worker.phoneNumber
        workerIsActiveLiveData.value = worker.isActive
    }

    fun setUser(user: User?){
        userLiveData.value = user
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
                    if (workerIdLiveData.value.isNullOrEmpty()) {
                        dataSource?.doCreateWorker(
                            userLiveData.value?.id!!,
                            workerNameLiveData.value!!,
                            workerAddressLiveData.value!!,
                            workerPhoneLiveData.value!!
                        )
                    } else {
                        dataSource?.doEditWorker(workerIdLiveData.value!!,
                            userLiveData.value?.id!!,
                            workerNameLiveData.value!!,
                            workerAddressLiveData.value!!,
                            workerPhoneLiveData.value!!,
                            workerIsActiveLiveData.value!!
                            )
                    }
                }

                when (response) {
                    is Result.Success -> {
                        when (response.data.errorCode) {
                            0 -> callback(0)
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
        if (workerNameLiveData.value?.trim().isNullOrEmpty()) {
            workerNameErrorLiveData.value = "Harap isi kolom ini"
            errorCount++
        } else {
            workerNameErrorLiveData.value = null
        }


        if (workerPhoneLiveData.value?.trim().isNullOrEmpty()) {
            workerPhoneErrorLiveData.value = "Harap isi kolom ini"
            errorCount++
        } else {
            workerPhoneErrorLiveData.value = null
        }

        if (workerAddressLiveData.value?.trim().isNullOrEmpty()) {
            workerAddressErrorLiveData.value = "Harap isi kolom ini"
            errorCount++
        } else {
            workerAddressErrorLiveData.value = null
        }


        return errorCount == 0
    }

}