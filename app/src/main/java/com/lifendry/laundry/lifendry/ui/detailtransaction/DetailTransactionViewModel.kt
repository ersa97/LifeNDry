package com.lifendry.laundry.lifendry.ui.detailtransaction

import androidx.lifecycle.MutableLiveData
import com.lifendry.laundry.lifendry.base.BaseViewModel
import com.lifendry.laundry.lifendry.model.transaction.Transaction

class DetailTransactionViewModel : BaseViewModel() {
    val transactionLiveData: MutableLiveData<Transaction> = MutableLiveData()
}