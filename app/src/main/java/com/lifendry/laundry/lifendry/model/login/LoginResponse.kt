package com.lifendry.laundry.lifendry.model.login

import com.google.gson.annotations.SerializedName
import com.lifendry.laundry.lifendry.base.BaseResponse
import com.lifendry.laundry.lifendry.model.user.User

data class LoginResponse(

    @SerializedName("data")
    var data: User? = null

) : BaseResponse()