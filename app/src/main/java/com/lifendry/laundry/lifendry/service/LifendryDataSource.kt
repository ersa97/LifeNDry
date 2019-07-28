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

class LifendryDataSource(private var api: ApiService? = null) {


    suspend fun doLogin(email: String?, password: String?) = safeApiCall {
        loginAsync(email, password)
    }

    private suspend fun loginAsync(email: String?, password: String?): Result<LoginResponse> {

        val response = api?.loginAsync(email, password)?.await()
        return if (response?.code() == 200) {
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(IOException())
            }
        } else {
            Result.Error(IOException())
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
        val response = api?.createCustomerAsync(email, name, phone, address)?.await()
        return if (response?.code() == 200) {
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Log.e("ERROR", response.message())
                Result.Error(IOException())
            }
        } else {
            Log.e("ERROR", response?.message())
            Result.Error(IOException())
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
            Result.Error(IOException())
        }
    }

    suspend fun doDeleteCustomer(pelangganId: String) = safeApiCall {
        deleteCustomerAsync(pelangganId)
    }

    private suspend fun deleteCustomerAsync(pelangganId: String): Result<CustomerResponse> {
        val response = api?.deleteCustomerAsync(pelangganId)?.await()
        return if (response?.isSuccessful == true) {
            Result.Success(response.body()!!)
        } else {
            Result.Error(IOException())
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
            Result.Error(IOException())
        }
    }

    suspend fun doSearchWorker(text: String, idBranch: String) = safeApiCall {
        searchWorkerAsync(text, idBranch)
    }

    private suspend fun searchWorkerAsync(text: String, idBranch: String): Result<WorkerListResponse> {
        val response = api?.searchWorkerAsync(text, idBranch)?.await()
        return if (response?.code() == 200) {
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(IOException())
            }
        } else {
            Result.Error(IOException())
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
            Result.Error(IOException())
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
            Result.Error(IOException())
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
            Result.Error(IOException())
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
            Result.Error(IOException())
        }
    }

    suspend fun doCreateTransaction(
        idServer: Int?,
        idBranch: String?,
        idCustomer: String?,
        idMenu: String?,
        quantity: Double?,
        info: String?
    ) = safeApiCall {
        createTransactionAsync(idServer, idBranch, idCustomer, idMenu, quantity, info)
    }

    private suspend fun createTransactionAsync(
        idServer: Int?,
        idBranch: String?,
        idCustomer: String?,
        idMenu: String?,
        quantity: Double?,
        info: String?
    ): Result<TransactionResponse> {
        val response = api?.createTransactionAsync(idServer, idBranch, idCustomer, idMenu, quantity, info)?.await()

        return if (response?.code() == 200) {
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(IOException())
            }
        } else {
            Result.Error(IOException())
        }
    }

    suspend fun doShowUnfinishedTransaction(idServer: Int?, idBranch: String?) = safeApiCall {
        showUnfinishedTransactionAsync(idServer, idBranch)
    }

    private suspend fun showUnfinishedTransactionAsync(idServer: Int?, idBranch: String?): Result<TransactionListResponse> {
        val response = api?.showUnfinishedTransactionAsync(idServer, idBranch)?.await()

        return if (response?.code() == 200) {
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(IOException())
            }
        } else {
            Result.Error(IOException())
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
            Result.Error(IOException())
        }
    }

    suspend fun doStartLaundryActivity(idServer : Int?, idLaundryActivity: String?, idWorker: String?) = safeApiCall {
        startLaundryActivityAsync(idServer, idLaundryActivity, idWorker)
    }

    private suspend fun startLaundryActivityAsync(
        idServer: Int?,
        idLaundryActivity: String?,
        idWorker: String?
    ): Result<ActivityLaundryResponse> {
        val response = api?.startLaundryActivityAsync(idServer, idLaundryActivity, idWorker)?.await()

        return if (response?.code() == 200) {
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(IOException())
            }
        } else {
            Result.Error(IOException())
        }
    }

    suspend fun doFinishLaundryActivity(idServer: Int?, idLaundryActivity: String?) = safeApiCall {
        finishLaundryActivityAsync(idServer, idLaundryActivity)
    }

    private suspend fun finishLaundryActivityAsync(idServer: Int?, idLaundryActivity: String?): Result<ActivityLaundryResponse> {
        val response = api?.finishLaundryActivityAsync(idServer, idLaundryActivity)?.await()

        return if (response?.code() == 200) {
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(IOException())
            }
        } else {
            Result.Error(IOException())
        }
    }

    suspend fun doPaidTransaction(idServer: Int?, id: String?) = safeApiCall {
        paidTransactionAsync(idServer, id)
    }

    private suspend fun paidTransactionAsync(idServer: Int?, id: String?): Result<TransactionResponse> {
        val response = api?.paidTransactionAsync(idServer, id)?.await()

        return if (response?.code() == 200) {
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(IOException())
            }
        } else {
            Result.Error(IOException())
        }
    }

    suspend fun doTakeTransaction(idServer: Int?, id: String?) = safeApiCall {
        takeTransactionAsync(idServer, id)
    }

    private suspend fun takeTransactionAsync(idServer: Int?, id: String?): Result<TransactionResponse> {
        val responnse = api?.takeTransactionAsync(idServer, id)?.await()
        return if (responnse?.code() == 200) {
            if (responnse.isSuccessful) {
                Result.Success(responnse.body()!!)
            } else {
                Result.Error(IOException())
            }
        } else {
            Result.Error(IOException())
        }
    }

    suspend fun doGetTransaction(id: String?) = safeApiCall {
        getTransactionAsync(id)
    }

    private suspend fun getTransactionAsync(id: String?): Result<TransactionResponse> {
        val responnse = api?.getTransactionAsync(id)?.await()
        return if (responnse?.code() == 200) {
            if (responnse.isSuccessful) {
                Result.Success(responnse.body()!!)
            } else {
                Result.Error(IOException())
            }
        } else {
            Result.Error(IOException())
        }
    }

    suspend fun doShowFinishedTransaction(idServer: Int?, id: String?) = safeApiCall {
        showFinishedTransactionAsync(idServer, id)
    }

    private suspend fun showFinishedTransactionAsync(idServer: Int?, id: String?): Result<TransactionListResponse> {
        val responnse = api?.showFinishedTransactionAsync(idServer, id)?.await()
        return if (responnse?.code() == 200) {
            if (responnse.isSuccessful) {
                Result.Success(responnse.body()!!)
            } else {
                Result.Error(IOException())
            }
        } else {
            Result.Error(IOException())
        }
    }


    private fun changeBaseUrl(newApi: ApiService) {
        api = null
        api = newApi
    }
}