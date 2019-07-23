package com.lifendry.laundry.lifendry.model.menu

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Menu (
    @SerializedName("id_menu")
    var id: String? = null,

    @SerializedName("nama")
    var name: String? = null,

    @SerializedName("deskripsi")
    var desc: String? = null,

    @SerializedName("minimum")
    var minimum: String? = null,

    @SerializedName("harga_menu")
    var price: Double = 0.0
): Parcelable