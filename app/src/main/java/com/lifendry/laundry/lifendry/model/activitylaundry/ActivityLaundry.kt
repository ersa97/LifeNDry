package com.lifendry.laundry.lifendry.model.activitylaundry

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ActivityLaundry(
    @SerializedName("id_aktivitas_laundry")
    var idActivityLaundry: String? = null,

    @SerializedName("id_detail_laundry")
    var idDetailLaundry: String? = null,

    @SerializedName("id_transaksi_laundry")
    var idTransactionLaundry: String? = null,

    @SerializedName("id_pegawai")
    var idWorker: String? = null,

    @SerializedName("nama_pegawai")
    var nameWorker: String? = null,

    @SerializedName("id_aktivitas")
    var idActivity: String? = null,

    @SerializedName("nama_aktivitas")
    var nameActivity: String? = null,

    @SerializedName("mulai_pengerjaan")
    var start: Date? = null,

    @SerializedName("selesai")
    var end: Date? = null
): Parcelable