package com.lifendry.laundry.lifendry.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lifendry.laundry.lifendry.base.BaseViewModel
import com.lifendry.laundry.lifendry.model.user.User
import com.lifendry.laundry.lifendry.service.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class LoginViewModel() : BaseViewModel() {

    var emailLiveData: MutableLiveData<String> = MutableLiveData()
    var passwordLiveData: MutableLiveData<String> = MutableLiveData()
    private var userLiveData: MutableLiveData<User> = MutableLiveData()

    private var errorLoginLiveData: MutableLiveData<String> = MutableLiveData()

    fun getEmailLiveData(): LiveData<String> {
        return emailLiveData
    }

    fun getPasswordLiveData(): LiveData<String> {
        return passwordLiveData
    }

    fun getUserLiveData(): LiveData<User> {
        return userLiveData
    }

    fun getErrorLoginLiveData(): LiveData<String> {
        return errorLoginLiveData
    }

    fun login() {
        if (validate()) {
            scope.launch {
                setLoading(true)
                val loginResponse = withContext(Dispatchers.IO) {
                    dataSource?.doLogin(emailLiveData.value, passwordLiveData.value)
                }


                when (loginResponse) {
                    is Result.Success -> {
                        if (loginResponse.data.errorCode == 0) {
                            userLiveData.value = loginResponse.data.data
                        } else if (loginResponse.data.errorCode == -1) {
                            errorLoginLiveData.value = "Email atau password salah"
                        }
                    }
                    is Result.Error -> {
                        if (loginResponse.exception is IOException) {
                            errorLoginLiveData.postValue("Terjadi kesalahan pada saat sign in")
                            Log.d(LoginViewModel::class.java.simpleName, loginResponse.exception.message)
                        }
                    }
                }
                setLoading(false)

            }
        } else {
            errorLoginLiveData.postValue("Harap isi kolom")
        }

    }

    private fun validate(): Boolean {
        return (!emailLiveData.value.isNullOrEmpty() && !passwordLiveData.value.isNullOrEmpty())
    }

}