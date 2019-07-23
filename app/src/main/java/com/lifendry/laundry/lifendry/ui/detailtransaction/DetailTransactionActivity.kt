package com.lifendry.laundry.lifendry.ui.detailtransaction

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lifendry.laundry.lifendry.BR
import com.lifendry.laundry.lifendry.R
import com.lifendry.laundry.lifendry.base.BaseActivity
import com.lifendry.laundry.lifendry.databinding.ActivityDetailTransactionBinding
import com.lifendry.laundry.lifendry.model.transaction.Transaction
import kotlinx.android.synthetic.main.activity_detail_transaction.*
import kotlinx.android.synthetic.main.template_toolbar.*

class DetailTransactionActivity : BaseActivity<ActivityDetailTransactionBinding, DetailTransactionViewModel>() {

    private lateinit var mDetailTransactionViewModel: DetailTransactionViewModel
    private lateinit var mDetailTransactionBinding: ActivityDetailTransactionBinding

    override fun getLayoutId(): Int {
        return R.layout.activity_detail_transaction
    }

    override fun getViewModel(): DetailTransactionViewModel {
        mDetailTransactionViewModel = ViewModelProviders.of(this).get(DetailTransactionViewModel::class.java)
        return mDetailTransactionViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.detailTransactionViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDetailTransactionBinding = getViewDataBinding()
        setUp()
    }

    private fun setUp() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btn_progress.setOnClickListener {

        }

        btn_paid.setOnClickListener {

        }

        btn_take.setOnClickListener {

        }

        if (intent.hasExtra(TRANSACTION_EXTRA)) {
            mDetailTransactionViewModel.transactionLiveData.postValue(intent.getParcelableExtra(TRANSACTION_EXTRA))
        }

        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        mDetailTransactionViewModel.transactionLiveData.observe(this, Observer {
            if (it != null) {
                txt_transaction_id.text = it.idTransaction
                txt_customer_name.text = "${it.idCustomer} - ${it.customer} - ${it.customerPhone}"
                txt_menu.text = "${it.idMenu} - ${it.menu}"
                txt_quantity.text = "${it.quantity} ${it.uom}"
                txt_cost.text = "Rp. ${it.price}"
                txt_is_paid.text = if (it.isPaid) "Sudah" else "Belum"
                txt_is_taken.text = if (it.isTaken) "Sudah" else "Belum"
                txt_info.text = it.info
            }
        })
    }

    companion object {
        fun newIntent(context: Context, transaction: Transaction): Intent =
            Intent(context, DetailTransactionActivity::class.java).apply {
                putExtra(TRANSACTION_EXTRA, transaction)
            }

        const val TRANSACTION_EXTRA = "TRANSACTION_EXTRA"
    }

}
