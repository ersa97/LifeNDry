package com.lifendry.laundry.lifendry.model.activitylaundry

import com.google.gson.annotations.SerializedName
import com.lifendry.laundry.lifendry.base.BaseResponse

data class ActivityLaundryResponse(

    @SerializedName("data")
    var activityLaundry: ActivityLaundry? = null
) : BaseResponse()