package com.lifendry.laundry.lifendry.model.worker

import com.google.gson.annotations.SerializedName
import com.lifendry.laundry.lifendry.base.BaseResponse

data class WorkerListResponse(

    @SerializedName("data")
    var data: List<Worker>? = null

) : BaseResponse()