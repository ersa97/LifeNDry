package com.lifendry.laundry.lifendry.model.customer

import com.google.gson.annotations.SerializedName
import com.lifendry.laundry.lifendry.base.BaseResponse

data class CustomerResponse (

    @SerializedName("data")
    var data: Customer? = null

) : BaseResponse()