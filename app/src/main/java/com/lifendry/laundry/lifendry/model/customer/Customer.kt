package com.lifendry.laundry.lifendry.model.customer

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Customer(

    @SerializedName("id_pelanggan")
    var id: String? = null,

    @SerializedName("nama")
    var name: String? = null,

    @SerializedName("email")
    var email: String? = null,

    @SerializedName("no_telepon")
    var phoneNumber: String? = null,

    @SerializedName("alamat")
    var address: String? = null

) : Parcelable