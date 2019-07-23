package com.lifendry.laundry.lifendry.model.menu

import com.google.gson.annotations.SerializedName
import com.lifendry.laundry.lifendry.base.BaseResponse

data class MenuResponse (

    @SerializedName("data")
    var data: Menu? = null

) : BaseResponse()