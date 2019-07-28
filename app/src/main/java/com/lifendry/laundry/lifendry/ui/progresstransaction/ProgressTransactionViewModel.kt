package com.lifendry.laundry.lifendry.ui.progresstransaction

import androidx.lifecycle.MutableLiveData
import com.lifendry.laundry.lifendry.base.BaseViewModel
import com.lifendry.laundry.lifendry.model.activitylaundry.ActivityLaundry
import com.lifendry.laundry.lifendry.service.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProgressTransactionViewModel : BaseViewModel() {
    val activityLaundryLiveData: MutableLiveData<List<ActivityLaundry>> = MutableLiveData()
    val laundryIdLiveData: MutableLiveData<String> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun setLaundryId(idLaundry: String?){
        if(laundryIdLiveData.value.isNullOrEmpty()){
            laundryIdLiveData.postValue(idLaundry)
            load()
        }
    }

    fun load() {
        scope.launch {
            setLoading(true)

            val response = withContext(Dispatchers.IO) {
                dataSource?.doShowLaundryActivity(laundryIdLiveData.value)
            }

            when (response) {
                is Result.Success -> {
                    activityLaundryLiveData.value = response.data.data
                }
                is Result.Error -> {
                    errorLiveData.postValue("Terjadi kesalahan pada load data")
                }
            }
            setLoading(false)
        }
    }

    fun doActivity(
        idLaundryActivity: String?,
        idWorker: String?,
        success: (ActivityLaundry) -> Unit,
        failed: (String) -> Unit
    ) {
        scope.launch {
            setLoading(true)

            val response = withContext(Dispatchers.IO) {
                dataSource?.doStartLaundryActivity(getServerLiveData().value?.idServer, idLaundryActivity, idWorker)
            }

            when (response) {
                is Result.Success -> {
                    response.data.activityLaundry?.let { success(it) }
                }

                is Result.Error -> {
                    failed("Terjadi Kesalahan")
                }
            }

            setLoading(false)
        }
    }

    fun finishActivity(idActivityLaundry: String?, success: (ActivityLaundry) -> Unit,
                       failed: (String) -> Unit
    ) {
        scope.launch {
            setLoading(true)

            val response = withContext(Dispatchers.IO) {
                dataSource?.doFinishLaundryActivity(getServerLiveData().value?.idServer, idActivityLaundry)
            }

            when (response) {
                is Result.Success -> {
                    response.data.activityLaundry?.let { success(it) }
                }

                is Result.Error -> {
                    failed("Terjadi Kesalahan")
                }
            }

            setLoading(false)
        }
    }
}