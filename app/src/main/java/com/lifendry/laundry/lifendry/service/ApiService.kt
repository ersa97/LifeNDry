package com.lifendry.laundry.lifendry.service

import com.lifendry.laundry.lifendry.model.customer.CustomersListResponse
import com.lifendry.laundry.lifendry.model.customer.CustomerResponse
import com.lifendry.laundry.lifendry.model.login.LoginResponse
import com.lifendry.laundry.lifendry.model.menu.MenuListResponse
import com.lifendry.laundry.lifendry.model.transaction.TransactionListResponse
import com.lifendry.laundry.lifendry.model.transaction.TransactionResponse
import com.lifendry.laundry.lifendry.model.worker.WorkerListResponse
import com.lifendry.laundry.lifendry.model.worker.WorkerResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("auth/login")
    @FormUrlEncoded
    fun loginAsync(@Field("email") email: String?, @Field("password") password: String?): Deferred<Response<LoginResponse>>

    @POST("customer/store")
    @FormUrlEncoded
    fun createCustomerAsync(@Field("email") email: String?, @Field("nama") name: String?, @Field("no_telepon") phone: String?, @Field("alamat") address: String?): Deferred<Response<CustomerResponse>>

    @PUT("customer/update")
    @FormUrlEncoded
    fun editCustomerAsync(@Field("id_pelanggan") id: String?, @Field("email") email: String?, @Field("nama") name: String?, @Field("no_telepon") phone: String?, @Field("alamat") address: String?): Deferred<Response<CustomerResponse>>

    @DELETE("customer/delete/{id_pelanggan}")
    fun deleteCustomerAsync(@Path("id_pelanggan") pelangganId: String): Deferred<Response<CustomerResponse>>

    @GET("customer/search")
    fun searchCustomerAsync(@Query("q") text: String): Deferred<Response<CustomersListResponse>>

    @GET("pegawai/searchByBranch")
    fun searchWorkerAsync(@Query("q") text: String, @Query("branch") idBranch: String): Deferred<Response<WorkerListResponse>>

    @POST("pegawai/store")
    @FormUrlEncoded
    fun createWorkerAsync(
        @Field("id_cabang") branchId: String,
        @Field("nama") name: String,
        @Field("alamat") address: String,
        @Field("no_telp") phone: String,
        @Field("is_active") is_active: Int = 1
    ): Deferred<Response<WorkerResponse>>

    @PUT("pegawai/update")
    @FormUrlEncoded
    fun editWorkerAsync(@Field("id_pegawai") workerId: String,
                        @Field("id_cabang") branchId: String,
                        @Field("nama") name: String,
                        @Field("alamat") address:String,
                        @Field("no_telp") phone: String,
                        @Field("is_active") isActive: Int): Deferred<Response<WorkerResponse>>

    @DELETE("pegawai/delete/{id_pegawai}")
    fun deleteWorkerAsync(@Path("id_pegawai") workerId: String): Deferred<Response<WorkerResponse>>

    @GET("menu/show")
    fun showMenuAsync() : Deferred<Response<MenuListResponse>>

    @POST("transaksi/store")
    @FormUrlEncoded
    fun createTransactionAsync(
        @Field("id_cabang") idBranch: String?,
        @Field("id_pelanggan") idCustomer: String?,
        @Field("id_menu") idMenu: String?,
        @Field("real_quantity") quantity: Double?,
        @Field("info") info: String?
    ) : Deferred<Response<TransactionResponse>>

    @GET("transaksi/showUnfinished")
    fun showUnfinishedTransactionAsync(@Query("id_cabang") idBranch: String?) : Deferred<Response<TransactionListResponse>>
}