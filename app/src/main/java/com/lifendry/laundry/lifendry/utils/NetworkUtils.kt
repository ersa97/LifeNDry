package com.lifendry.laundry.lifendry.utils

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtils {
    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

    val BASE_URL = arrayListOf("http:/pdt1.himastika.me/api/", "http://pdt1.himastika.me/api/", "http://pdt3.himastika.me/api/")

}