package com.lifendry.laundry.lifendry.ui.worker.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lifendry.laundry.lifendry.base.BaseViewModel
import com.lifendry.laundry.lifendry.model.worker.Worker
import com.lifendry.laundry.lifendry.service.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailWorkerViewModel : BaseViewModel() {
    private val workerLiveData: MutableLiveData<Worker> = MutableLiveData()

    fun getWorkerLiveData(): LiveData<Worker> {
        return workerLiveData
    }

    fun setWorker(worker: Worker) {
        workerLiveData.value = worker
    }

    fun delete(callback: (Int) -> Unit){
        scope.launch {
            setLoading(true)

            val response = withContext(Dispatchers.IO){
                dataSource?.doDeleteWorker(workerLiveData.value?.idWorker!!)
            }

            when(response){
                is Result.Success -> {
                    when(response.data.errorCode){
                        0 -> callback(0)
                        -1 -> callback(-1)
                    }
                }

                is Result.Error -> {
                    Log.d(DetailWorkerViewModel::class.java.simpleName, response.exception.message)
                    callback(-99)
                }
            }

            setLoading(false)
        }
    }
}