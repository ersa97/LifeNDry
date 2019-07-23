package com.lifendry.laundry.lifendry.ui.main.transaction


import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.lifendry.laundry.lifendry.BR

import com.lifendry.laundry.lifendry.R
import com.lifendry.laundry.lifendry.base.BaseFragment
import com.lifendry.laundry.lifendry.databinding.FragmentTransactionBinding
import com.lifendry.laundry.lifendry.ui.customer.CustomerActivity
import com.lifendry.laundry.lifendry.ui.newtransaction.NewTransactionActivity
import kotlinx.android.synthetic.main.fragment_transaction.*

class TransactionFragment : BaseFragment<FragmentTransactionBinding, TransactionViewModel>() {

    private lateinit var mTransactionViewModel: TransactionViewModel
    private lateinit var mTransactionBinding: FragmentTransactionBinding

    override fun getLayoutId(): Int {
        return R.layout.fragment_transaction
    }

    override fun getViewModel(): TransactionViewModel {
        mTransactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel::class.java)
        return mTransactionViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.transactionViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mTransactionBinding = getViewDataBinding()
        setUp()
    }

    private fun setUp(){
        btn_pelanggan.setOnClickListener {
            startActivity(CustomerActivity.newIntent(requireContext()))
        }

        btn_transaksi.setOnClickListener {
            startActivity(NewTransactionActivity.newIntent(requireContext()))
        }
    }
}
