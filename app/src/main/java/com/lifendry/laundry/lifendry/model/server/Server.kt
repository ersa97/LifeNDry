package com.lifendry.laundry.lifendry.model.server

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Server(
    var idServer: Int = 0,
    var ip: String? = null
) : Parcelable