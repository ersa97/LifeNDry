package com.lifendry.laundry.lifendry.model.activitylaundry

import com.google.gson.annotations.SerializedName
import com.lifendry.laundry.lifendry.base.BaseResponse

data class ActivityLaundryListResponse (

    @SerializedName("data")
    var data: List<ActivityLaundry>? = null

) : BaseResponse()