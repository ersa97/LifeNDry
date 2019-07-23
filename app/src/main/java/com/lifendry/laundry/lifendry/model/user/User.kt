package com.lifendry.laundry.lifendry.model.user

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    var id: String? = null,

    @SerializedName("email")
    var email: String? = null,

    @SerializedName("password")
    var password: String? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("roleId")
    var roleId: Int = 0
)