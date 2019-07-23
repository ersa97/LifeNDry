package com.lifendry.laundry.lifendry.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lifendry.laundry.lifendry.service.LifendryDataSource
import com.lifendry.laundry.lifendry.service.LifendryRetrofit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

open class BaseViewModel : ViewModel() {

    protected var dataSource: LifendryDataSource? = LifendryDataSource()

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    protected val scope = CoroutineScope(coroutineContext)

    private val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun setLoading(state: Boolean) {
        loadingLiveData.postValue(state)
    }

    fun isLoading(): LiveData<Boolean> = loadingLiveData

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
        dataSource = null
    }
}