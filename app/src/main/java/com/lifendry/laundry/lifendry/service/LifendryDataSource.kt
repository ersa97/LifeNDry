package com.lifendry.laundry.lifendry.service

import android.util.Log
import com.lifendry.laundry.lifendry.model.activitylaundry.ActivityLaundryListResponse
import com.lifendry.laundry.lifendry.model.activitylaundry.ActivityLaundryResponse
import com.lifendry.laundry.lifendry.model.customer.CustomerResponse
import com.lifendry.laundry.lifendry.model.customer.CustomersListResponse
import com.lifendry.laundry.lifendry.model.login.LoginResponse
import com.lifendry.laundry.lifendry.model.menu.MenuListResponse
import com.lifendry.laundry.lifendry.model.transaction.TransactionListResponse
import com.lifendry.laundry.lifendry.model.transaction.TransactionResponse
import com.lifendry.laundry.lifendry.model.worker.WorkerListResponse
import com.lifendry.laundry.lifendry.model.worker.WorkerResponse
import com.lifendry.laundry.lifendry.utils.NetworkUtils
import com.lifendry.laundry.lifendry.utils.safeApiCall
import java.io.IOException
import java.net.SocketTimeoutException

class LifendryDataSource(private var api: ApiService? = LifendryRetrofit.api(NetworkUtils.BASE_URL[0])) {

    private var indexBaseUrl = 0
    private var failedCount = 0

    private fun incrementBaseUrl(): Int {
        if (indexBaseUrl < 3) {
            indexBaseUrl++
            failedCount++
        } else {
            indexBaseUrl = 0
        }
        return indexBaseUrl
    }

    suspend fun doLogin(email: String?, password: String?) = safeApiCall {
        loginAsync(email, password)
    }

    private suspend fun loginAsync(email: String?, password: String?): Result<LoginResponse> {

        try {
            val response = api?.loginAsync(email, password)?.await()
            return if (response?.code() == 200) {
                if (response.isSuccessful) {
                    Result.Success(response.body()!!)
                } else {
                    Result.Error(IOException())
                }
            } else {
                this.changeBaseUrl(
                    LifendryRetrofit.api(NetworkUtils.BASE_URL[incrementBaseUrl()])
                )
                loginAsync(email, password)
            }
        } catch (e: Exception) {
            Log.e("Service", e.message)
            return loginAsync(email, password)
        }


    }

    suspend fun doCreateCustomer(email: String?, name: String?, phone: String?, address: String?) = safeApiCall {
        createCustomerAsync(email, name, phone, address)
    }

    private suspend fun createCustomerAsync(
        email: String?,
        name: String?,
        phone: String?,
        address: String?
    ): Result<CustomerResponse> {
        try {
            val response = api?.createCustomerAsync(email, name, phone, address)?.await()
            return if (response?.code() == 200) {
                if (response.isSuccessful) {
                    Result.Success(response.body()!!)
                } else {
                    Log.e("ERROR", response.message())
                    Result.Error(IOException())
                }
            } else {
                this.changeBaseUrl(
                    LifendryRetrofit.api(NetworkUtils.BASE_URL[incrementBaseUrl()])
                )
                createCustomerAsync(email, name, phone, address)
            }
        } catch (e: Exception) {
            Log.d("EXCEPTIOn", e.message)
            e.printStackTrace()
            return Result.Error(IOException())
        }
    }

    suspend fun doEditCustomer(id: String?, email: String?, name: String?, phone: String?, address: String?) =
        safeApiCall {
            editCustomerAsync(id, email, name, phone, address)
        }

    private suspend fun editCustomerAsync(
        id: String?,
        email: String?,
        name: String?,
        phone: String?,
        address: String?
    ): Result<CustomerResponse> {
        val response = api?.editCustomerAsync(id, email, name, phone, address)?.await()
        return if (response?.code() == 200) {
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(IOException())
            }
        } else {
            this.changeBaseUrl(
                LifendryRetrofit.api(NetworkUtils.BASE_URL[incrementBaseUrl()])
            )
            editCustomerAsync(id, email, name, phone, address)
        }
    }

    suspend fun doDeleteCustomer(pelangganId: String) = safeApiCall {
        deleteCustomerAsync(pelangganId)
    }

    private suspend fun deleteCustomerAsync(pelangganId: String): Result<CustomerResponse> {
        try {
            val response = api?.deleteCustomerAsync(pelangganId)?.await()
            return if (response?.isSuccessful == true) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(IOException())
            }
        } catch (e: SocketTimeoutException) {
            return if (failedCount < 3) {
                this.changeBaseUrl(
                    LifendryRetrofit.api(NetworkUtils.BASE_URL[incrementBaseUrl()])
                )
                deleteCustomerAsync(pelangganId)
            } else {
                failedCount = 0
                Result.Error(IOException())
            }
        }
    }

    suspend fun doSearchCustomer(text: String) = safeApiCall {
        searchCustomerAsync(text)
    }

    private suspend fun searchCustomerAsync(text: String): Result<CustomersListResponse> {
        val response = api?.searchCustomerAsync(text)?.await()
        return if (response?.code() == 200) {
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(IOException())
            }
        } else {
            this.changeBaseUrl(
                LifendryRetrofit.api(NetworkUtils.BASE_URL[incrementBaseUrl()])
            )
            searchCustomerAsync(text)
        }
    }

    suspend fun doSearchWorker(text: String, idBranch: String) = safeApiCall {
        searchWorkerAsync(text, idBranch)
    }

    private suspend fun searchWorkerAsync(text: String, idBranch: String): Result<WorkerListResponse> {
        return try {
            val response = api?.searchWorkerAsync(text, idBranch)?.await()
            if (response?.code() == 200) {
                if (response.isSuccessful) {
                    Result.Success(response.body()!!)
                } else {
                    Result.Error(IOException())
                }
            } else {
                this.changeBaseUrl(
                    LifendryRetrofit.api(NetworkUtils.BASE_URL[incrementBaseUrl()])
                )
                searchWorkerAsync(text, idBranch)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Error", e.message)
            return Result.Error(e)
        }
    }

    suspend fun doCreateWorker(branchId: String, name: String, address: String, phone: String) = safeApiCall {
        createWorkerAsync(
            branchId,
            name,
            address,
            phone
        )
    }

    private suspend fun createWorkerAsync(
        branchId: String,
        name: String,
        address: String,
        phone: String
    ): Result<WorkerResponse> {
        try {
            val response = api?.createWorkerAsync(
                branchId,
                name,
                address,
                phone,
                1
            )?.await()
            return if (response?.code() == 200) {
                if (response.isSuccessful) {
                    Result.Success(response.body()!!)
                } else {
                    Result.Error(IOException())
                }
            } else {
                this.changeBaseUrl(
                    LifendryRetrofit.api(NetworkUtils.BASE_URL[incrementBaseUrl()])
                )
                createWorkerAsync(
                    branchId,
                    name,
                    address,
                    phone
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Error", e.message)
            return Result.Error(e)
        }

    }

    suspend fun doEditWorker(
        workerId: String,
        branchId: String,
        name: String,
        address: String,
        phone: String,
        isActive: Int
    ) = safeApiCall {
        editWorkerAsync(
            workerId,
            branchId,
            name,
            address,
            phone,
            isActive
        )
    }

    private suspend fun editWorkerAsync(
        workerId: String,
        branchId: String,
        name: String,
        address: String,
        phone: String,
        isActive: Int
    ): Result<WorkerResponse> {
        val response = api?.editWorkerAsync(
            workerId,
            branchId,
            name,
            address,
            phone,
            isActive
        )?.await()
        return if (response?.code() == 200) {
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(IOException())
            }
        } else {
            this.changeBaseUrl(
                LifendryRetrofit.api(NetworkUtils.BASE_URL[incrementBaseUrl()])
            )
            editWorkerAsync(
                workerId,
                branchId,
                name,
                address,
                phone,
                isActive
            )
        }
    }

    suspend fun doDeleteWorker(workerId: String) = safeApiCall {
        deleteWorkerAsync(workerId)
    }

    private suspend fun deleteWorkerAsync(workerId: String): Result<WorkerResponse> {
        val response = api?.deleteWorkerAsync(workerId)?.await()
        return if (response?.code() == 200) {
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(IOException())
            }
        } else {
            this.changeBaseUrl(
                LifendryRetrofit.api(NetworkUtils.BASE_URL[incrementBaseUrl()])
            )
            deleteWorkerAsync(workerId)
        }
    }

    suspend fun doShowMenu() = safeApiCall {
        showMenuAsync()
    }

    private suspend fun showMenuAsync(): Result<MenuListResponse> {
        val response = api?.showMenuAsync()?.await()
        return if (response?.code() == 200) {
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(IOException())
            }
        } else {
            this.changeBaseUrl(
                LifendryRetrofit.api(NetworkUtils.BASE_URL[incrementBaseUrl()])
            )
            showMenuAsync()
        }
    }

    suspend fun doCreateTransaction(
        idBranch: String?,
        idCustomer: String?,
        idMenu: String?,
        quantity: Double?,
        info: String?
    ) = safeApiCall {
        createTransactionAsync(idBranch, idCustomer, idMenu, quantity, info)
    }

    private suspend fun createTransactionAsync(
        idBranch: String?,
        idCustomer: String?,
        idMenu: String?,
        quantity: Double?,
        info: String?
    ): Result<TransactionResponse> {
        val response = api?.createTransactionAsync(idBranch, idCustomer, idMenu, quantity, info)?.await()

        return if (response?.code() == 200) {
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(IOException())
            }
        } else {
            this.changeBaseUrl(
                LifendryRetrofit.api(NetworkUtils.BASE_URL[incrementBaseUrl()])
            )
            createTransactionAsync(idBranch, idCustomer, idMenu, quantity, info)
        }
    }

    suspend fun doShowUnfinishedTransaction(idBranch: String?) = safeApiCall {
        showUnfinishedTransactionAsync(idBranch)
    }

    private suspend fun showUnfinishedTransactionAsync(idBranch: String?): Result<TransactionListResponse> {
        val response = api?.showUnfinishedTransactionAsync(idBranch)?.await()

        return if (response?.code() == 200) {
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(IOException())
            }
        } else {
            this.changeBaseUrl(
                LifendryRetrofit.api(NetworkUtils.BASE_URL[incrementBaseUrl()])
            )
            showUnfinishedTransactionAsync(idBranch)
        }
    }

    suspend fun doShowLaundryActivity(idTransaction: String?) = safeApiCall {
        showLaundryActivityAsync(idTransaction)
    }

    private suspend fun showLaundryActivityAsync(idTransaction: String?): Result<ActivityLaundryListResponse> {
        val response = api?.showLaundryActivityAsync(idTransaction)?.await()

        return if (response?.code() == 200) {
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(IOException())
            }
        } else {
            this.changeBaseUrl(
                LifendryRetrofit.api(NetworkUtils.BASE_URL[incrementBaseUrl()])
            )
            showLaundryActivityAsync(idTransaction)
        }
    }

    suspend fun doStartLaundryActivity(idLaundryActivity: String?, idWorker: String?) = safeApiCall {
        startLaundryActivityAsync(idLaundryActivity, idWorker)
    }

    private suspend fun startLaundryActivityAsync(idLaundryActivity: String?, idWorker: String?): Result<ActivityLaundryResponse> {
        val response = api?.startLaundryActivityAsync(idLaundryActivity, idWorker)?.await()

        return if (response?.code() == 200) {
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(IOException())
            }
        } else {
            this.changeBaseUrl(
                LifendryRetrofit.api(NetworkUtils.BASE_URL[incrementBaseUrl()])
            )
            startLaundryActivityAsync(idLaundryActivity, idWorker)
        }
    }

    suspend fun doFinishLaundryActivity(idLaundryActivity: String?) = safeApiCall {
        finishLaundryActivityAsync(idLaundryActivity)
    }

    private suspend fun finishLaundryActivityAsync(idLaundryActivity: String?): Result<ActivityLaundryResponse> {
        val response = api?.finishLaundryActivityAsync(idLaundryActivity)?.await()

        return if (response?.code() == 200) {
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(IOException())
            }
        } else {
            this.changeBaseUrl(
                LifendryRetrofit.api(NetworkUtils.BASE_URL[incrementBaseUrl()])
            )
            finishLaundryActivityAsync(idLaundryActivity)
        }
    }

    suspend fun doPaidTransaction(id: String?) = safeApiCall {
        paidTransactionAsync(id)
    }

    private suspend fun paidTransactionAsync(id: String?) : Result<TransactionResponse> {
        val response = api?.paidTransactionAsync(id)?.await()

        return if(response?.code() == 200){
            if(response.isSuccessful){
                Result.Success(response.body()!!)
            } else {
                Result.Error(IOException())
            }
        } else {
            this.changeBaseUrl(LifendryRetrofit.api(NetworkUtils.BASE_URL[incrementBaseUrl()]))
            paidTransactionAsync(id)
        }
    }

    suspend fun doTakeTransaction(id: String?) = safeApiCall {
        takeTransactionAsync(id)
    }

    private suspend fun takeTransactionAsync(id: String?): Result<TransactionResponse> {
        val responnse = api?.takeTransactionAsync(id)?.await()
        return if(responnse?.code() == 200){
            if(responnse.isSuccessful){
                Result.Success(responnse.body()!!)
            } else {
                Result.Error(IOException())
            }
        } else {
            this.changeBaseUrl(LifendryRetrofit.api(NetworkUtils.BASE_URL[incrementBaseUrl()]))
            takeTransactionAsync(id)
        }
    }

    suspend fun doGetTransaction(id: String?) = safeApiCall {
        getTransactionAsync(id)
    }

    private suspend fun getTransactionAsync(id: String?): Result<TransactionResponse> {
        val responnse = api?.getTransactionAsync(id)?.await()
        return if(responnse?.code() == 200){
            if(responnse.isSuccessful){
                Result.Success(responnse.body()!!)
            } else {
                Result.Error(IOException())
            }
        } else {
            this.changeBaseUrl(LifendryRetrofit.api(NetworkUtils.BASE_URL[incrementBaseUrl()]))
            getTransactionAsync(id)
        }
    }

    suspend fun doShowFinishedTransaction(id: String?) = safeApiCall {
        showFinishedTransactionAsync(id)
    }

    private suspend fun showFinishedTransactionAsync(id: String?): Result<TransactionListResponse> {
        val responnse = api?.showFinishedTransactionAsync(id)?.await()
        return if(responnse?.code() == 200){
            if(responnse.isSuccessful){
                Result.Success(responnse.body()!!)
            } else {
                Result.Error(IOException())
            }
        } else {
            this.changeBaseUrl(LifendryRetrofit.api(NetworkUtils.BASE_URL[incrementBaseUrl()]))
            showFinishedTransactionAsync(id)
        }
    }


    private fun changeBaseUrl(newApi: ApiService) {
        api = null
        api = newApi
    }
}