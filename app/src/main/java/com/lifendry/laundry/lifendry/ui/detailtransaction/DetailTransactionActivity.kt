package com.lifendry.laundry.lifendry.ui.detailtransaction

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lifendry.laundry.lifendry.BR
import com.lifendry.laundry.lifendry.R
import com.lifendry.laundry.lifendry.base.BaseActivity
import com.lifendry.laundry.lifendry.databinding.ActivityDetailTransactionBinding
import com.lifendry.laundry.lifendry.model.transaction.Transaction
import com.lifendry.laundry.lifendry.ui.progresstransaction.ProgressTransactionActivity
import com.lifendry.laundry.lifendry.utils.update
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUp() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mDetailTransactionViewModel.setServerLiveData(getServerPreference().server)
        btn_progress.setOnClickListener {
            startActivityForResult(ProgressTransactionActivity.newIntent(this, mDetailTransactionViewModel.transactionLiveData.value?.idTransaction), PROGRESS_CODE)
        }

        btn_paid.setOnClickListener {
            mDetailTransactionViewModel.paid{
                callbackPaid(it)
            }
        }

        btn_take.setOnClickListener {
            mDetailTransactionViewModel.take {
                callbackTake(it)
            }
        }

        if (intent.hasExtra(TRANSACTION_EXTRA)) {
            mDetailTransactionViewModel.transactionLiveData.postValue(intent.getParcelableExtra(TRANSACTION_EXTRA))
        }

        subscribeToLiveData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PROGRESS_CODE && resultCode == Activity.RESULT_OK){
            mDetailTransactionViewModel.reload()
        }
    }

    private fun subscribeToLiveData() {
        mDetailTransactionViewModel.transactionLiveData.observe(this, Observer {
            if (it != null) {
                txt_transaction_id.text = it.idTransaction
                txt_customer_name.text = "${it.idCustomer} - ${it.customer} - ${it.customerPhone}"
                txt_menu.text = "${it.idMenu} - ${it.menu}"
                txt_quantity.text = "${it.quantity} ${it.uom}"
                txt_cost.text = "Rp. ${it.price}"
                txt_is_paid.text = if (it.isPaid == 1) "Sudah" else "Belum"
                txt_is_taken.text = if (it.isTaken == 1) "Sudah ${it.takeTime}" else "Belum"
                txt_info.text = it.info

                btn_take.isEnabled = it.isPaid == 1 && it.isTaken == 0 && it.done == 1
                btn_paid.isEnabled = it.isPaid == 0
            }
        })
    }


    private fun callbackPaid(errorCode: Int){
        when(errorCode){
            0 -> {
                setResult(Activity.RESULT_OK)
                Toast.makeText(this, "Pembayaran berhasil dilakukan", Toast.LENGTH_SHORT).show()
            }
            -99 -> {
                Toast.makeText(this, "Terjadi kesalahan, silahkan coba lagi.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun callbackTake(errorCode: Int){
        when(errorCode){
            0 -> {
                setResult(Activity.RESULT_OK)
                Toast.makeText(this, "Pengambilan laundry berhasil dilakukan", Toast.LENGTH_SHORT).show()
            }

            -1 -> {
                Toast.makeText(this, "Laundry harus dibayar terlebih dahulu", Toast.LENGTH_SHORT).show()
            }

            -2 -> {
                Toast.makeText(this, "Laundry belum selesai dikerjakan, tidak bisa diambil", Toast.LENGTH_SHORT).show()
            }

            -98 -> {
                Toast.makeText(this, "Id Transaksi Laundry tidak dikenal", Toast.LENGTH_SHORT).show()
            }

            -99 -> {
                Toast.makeText(this, "Terjadi kesalahan, silahkan coba lagi.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        fun newIntent(context: Context, transaction: Transaction): Intent =
            Intent(context, DetailTransactionActivity::class.java).apply {
                putExtra(TRANSACTION_EXTRA, transaction)
            }

        const val TRANSACTION_EXTRA = "TRANSACTION_EXTRA"
        const val PROGRESS_CODE = 1
    }

}