package com.lifendry.laundry.lifendry.model.menu

import com.google.gson.annotations.SerializedName
import com.lifendry.laundry.lifendry.base.BaseResponse

data class MenuListResponse(

    @SerializedName("data")
    var data: List<Menu>? = null

) : BaseResponse()