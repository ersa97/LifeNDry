package com.lifendry.laundry.lifendry.model.customer

import com.google.gson.annotations.SerializedName
import com.lifendry.laundry.lifendry.base.BaseResponse

data class CustomersListResponse (

    @SerializedName("data")
    var data: List<Customer>? = null

) : BaseResponse()