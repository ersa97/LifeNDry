package com.lifendry.laundry.lifendry.utils

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtils {
    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

    val BASE_URL = arrayListOf("http://192.168.137.1:8000/api/", "http://192.168.137.1:8000/api/", "http://192.168.137.1:8000/api/")

}