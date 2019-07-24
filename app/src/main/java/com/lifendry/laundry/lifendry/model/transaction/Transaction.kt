package com.lifendry.laundry.lifendry.model.transaction

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Transaction (
    @SerializedName("id_transaksi_laundry")
    var idTransaction: String? = null,

    @SerializedName("id_cabang")
    var idBranch: String? = null,

    @SerializedName("tanggal")
    var date: Date? = null,

    @SerializedName("id_detail_laundry")
    var idDetailTransaction: String? = null,

    @SerializedName("id_pelanggan")
    var idCustomer: String? = null,

    @SerializedName("nama_pelanggan")
    var customer: String? = null,

    @SerializedName("no_telepon_pelanggan")
    var customerPhone: String? = null,

    @SerializedName("id_menu")
    var idMenu: String? = null,

    @SerializedName("nama")
    var menu: String? = null,

    @SerializedName("quantity")
    var quantity: Double = 0.0,

    @SerializedName("satuan")
    var uom: String? = null,

    @SerializedName("total_harga")
    var price: Double = 0.0,

    @SerializedName("is_paid")
    var isPaid: Int = 0,

    @SerializedName("is_taken")
    var isTaken: Int = 0,

    @SerializedName("waktu_pengambilan")
    var takeTime: Date? = null,

    @SerializedName("info")
    var info: String? = null,

    @SerializedName("selesai_dikerjakan")
    var done: Int = 0

) : Parcelable