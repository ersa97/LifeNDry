package com.lifendry.laundry.lifendry.model.worker

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Worker(
    @SerializedName("id_pegawai")
    var idWorker: String? = null,

    @SerializedName("nama")
    var name: String? = null,

    @SerializedName("alamat")
    var address: String? = null,

    @SerializedName("no_telp")
    var phoneNumber: String? = null,

    @SerializedName("id_cabang")
    var idBranch: String? = null,

    @SerializedName("is_active")
    var isActive: Int = 0
) : Parcelable {
    fun getIsActive() : Boolean{
        return isActive == 1
    }
}