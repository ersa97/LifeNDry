package com.lifendry.laundry.lifendry.ui.customer.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lifendry.laundry.lifendry.BR
import com.lifendry.laundry.lifendry.R
import com.lifendry.laundry.lifendry.base.BaseActivity
import com.lifendry.laundry.lifendry.databinding.ActivityDetailCustomerBinding
import com.lifendry.laundry.lifendry.model.customer.Customer
import com.lifendry.laundry.lifendry.ui.customer.form.FormCustomerActivity
import kotlinx.android.synthetic.main.activity_detail_customer.*
import kotlinx.android.synthetic.main.template_toolbar.*

class DetailCustomerActivity : BaseActivity<ActivityDetailCustomerBinding, DetailCustomerViewModel>() {

    private lateinit var mDetailCustomerViewModel: DetailCustomerViewModel
    private lateinit var mDetailCustomerBinding: ActivityDetailCustomerBinding

    override fun getLayoutId(): Int {
        return R.layout.activity_detail_customer
    }

    override fun getViewModel(): DetailCustomerViewModel {
        mDetailCustomerViewModel = ViewModelProviders.of(this).get(DetailCustomerViewModel::class.java)
        return mDetailCustomerViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.detailCustomerViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDetailCustomerBinding = getViewDataBinding()
        setUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(intent.getBooleanExtra(CUSTOMER_LOOKUP, false)){
            menuInflater.inflate(R.menu.menu_save, menu)
        } else {
            menuInflater.inflate(R.menu.menu_edit_delete, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CUSTOMER_EDIT && resultCode == RESULT_OK) {
            val customer = data?.getParcelableExtra<Customer>(FormCustomerActivity.CUSTOMER_EDIT_EXTRA)
            customer?.let {
                mDetailCustomerViewModel.setCustomer(it)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            R.id.menu_edit -> {
                startActivityForResult(
                    FormCustomerActivity.newIntent(
                        this,
                        mDetailCustomerViewModel.getCustomerLiveData().value
                    ), CUSTOMER_EDIT
                )
                true
            }

            R.id.menu_delete -> {
                mDetailCustomerViewModel.delete {
                    doCallback(it)
                }
                true
            }

            R.id.menu_save -> {
                setResult(
                    Activity.RESULT_OK,
                    Intent().putExtra(
                        CUSTOMER_EXTRA,
                        mDetailCustomerViewModel.getCustomerLiveData().value
                    )
                )
                finish()
                true

            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun doCallback(errorCode: Int) {
        when (errorCode) {
            0 -> {
                Toast.makeText(
                    this, "Pelanggan berhasil dihapus",
                    Toast.LENGTH_SHORT
                ).show()
                setResult(RESULT_OK)
                finish()
            }
            -1 -> {
                Toast.makeText(
                    this, "Pelanggan sudah pernah melakukan transaksi, proses hapus dibatalkan",
                    Toast.LENGTH_SHORT
                ).show()
            }
            -99 -> {
                Toast.makeText(this, "Proses gagal, terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUp() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mDetailCustomerViewModel.setCustomer(intent.getParcelableExtra(CUSTOMER_EXTRA))
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        mDetailCustomerViewModel.getCustomerLiveData().observe(this, Observer {
            txt_customer_name.text = it.name
            txt_customer_email.text = it.email
            txt_customer_address.text = it.address
            txt_customer_phone.text = it.phoneNumber
        })
    }

    companion object {
        fun newIntent(context: Context, customer: Customer): Intent =
            Intent(context, DetailCustomerActivity::class.java).apply {
                putExtra(CUSTOMER_EXTRA, customer)
            }

        fun newIntentLookUp(context: Context, customer: Customer): Intent = Intent(context, DetailCustomerActivity::class.java).apply{
            putExtra(CUSTOMER_EXTRA, customer)
            putExtra(CUSTOMER_LOOKUP, true)

        }
        const val CUSTOMER_LOOKUP = "CUSTOMER_LOOKUP"
        const val CUSTOMER_EXTRA = "CUSTOMER_EXTRA"
        const val CUSTOMER_EDIT = 1
    }
}
