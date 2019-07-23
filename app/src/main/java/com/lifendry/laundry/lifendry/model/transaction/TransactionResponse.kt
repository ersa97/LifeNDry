package com.lifendry.laundry.lifendry.model.transaction

import com.google.gson.annotations.SerializedName
import com.lifendry.laundry.lifendry.base.BaseResponse

data class TransactionResponse(

    @SerializedName("data")
    var data: Transaction? = null

) : BaseResponse()