package com.lifendry.laundry.lifendry.base

import com.google.gson.annotations.SerializedName

abstract class BaseResponse {
    @SerializedName("errorCode")
    var errorCode : Int = 0
}