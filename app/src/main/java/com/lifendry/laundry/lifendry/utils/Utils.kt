package com.lifendry.laundry.lifendry.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.io.IOException
import com.lifendry.laundry.lifendry.service.Result


suspend fun <T : Any> safeApiCall(call: suspend () -> Result<T>): Result<T> = try {
    call.invoke()
} catch (e: Exception) {
    Result.Error(IOException(e))
}

fun String.isEmail() : Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}


fun <T> MutableLiveData<T>.update() {
    this.value = this.value
}