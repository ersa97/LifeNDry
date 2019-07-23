package com.lifendry.laundry.lifendry.ui.newtransaction

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
import com.lifendry.laundry.lifendry.databinding.ActivityNewTransactionBinding
import com.lifendry.laundry.lifendry.ui.customer.CustomerActivity
import com.lifendry.laundry.lifendry.ui.customer.detail.DetailCustomerActivity
import com.lifendry.laundry.lifendry.ui.detailtransaction.DetailTransactionActivity
import com.lifendry.laundry.lifendry.ui.info.InfoActivity
import com.lifendry.laundry.lifendry.ui.menu.MenuActivity
import com.lifendry.laundry.lifendry.ui.menu.detail.DetailMenuActivity
import com.lifendry.laundry.lifendry.ui.quantity.QuantityActivity
import kotlinx.android.synthetic.main.activity_new_transaction.*
import kotlinx.android.synthetic.main.template_toolbar.*

class NewTransactionActivity : BaseActivity<ActivityNewTransactionBinding, NewTransactionViewModel>() {

    private lateinit var mNewTransactionViewModel: NewTransactionViewModel
    private lateinit var mNewTransactionBinding: ActivityNewTransactionBinding

    override fun getLayoutId(): Int {
        return R.layout.activity_new_transaction
    }

    override fun getViewModel(): NewTransactionViewModel {
        mNewTransactionViewModel = ViewModelProviders.of(this).get(NewTransactionViewModel::class.java)
        return mNewTransactionViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.newTransactionViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mNewTransactionBinding = getViewDataBinding()
        setUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CUSTOMER_LOOKUP_CODE && resultCode == RESULT_OK) {
            if (data?.hasExtra(DetailCustomerActivity.CUSTOMER_EXTRA) == true) {
                mNewTransactionViewModel.customerLiveData.postValue(data.getParcelableExtra(DetailCustomerActivity.CUSTOMER_EXTRA))
            } else {
                mNewTransactionViewModel.customerLiveData.postValue(null)
            }
        } else if (requestCode == MENU_LOOKUP_CODE && resultCode == RESULT_OK) {
            if (data?.hasExtra(DetailMenuActivity.MENU_EXTRA) == true) {
                mNewTransactionViewModel.menuLiveData.postValue(data.getParcelableExtra(DetailMenuActivity.MENU_EXTRA))
            } else {
                mNewTransactionViewModel.menuLiveData.postValue(null)
            }
        } else if (requestCode == QUANTITY_LOOKUP_CODE && resultCode == Activity.RESULT_OK) {
            if (data?.hasExtra(QuantityActivity.QUANTITY_EXTRA) == true) {
                mNewTransactionViewModel.quantityLiveData.postValue(
                    data.getDoubleExtra(
                        QuantityActivity.QUANTITY_EXTRA,
                        0.0
                    )
                )
            }
        } else if (requestCode == INFO_LOOKUP_CODE && resultCode == RESULT_OK) {
            if (data?.hasExtra(InfoActivity.INFO_EXTRA) == true) {
                val d = data.getStringExtra(InfoActivity.INFO_EXTRA)
                mNewTransactionViewModel.infoLiveData.postValue(if (d.isEmpty()) "-" else d)
            } else {
                mNewTransactionViewModel.infoLiveData.postValue("-")
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            R.id.menu_save -> {
                mNewTransactionViewModel.submit({
                    doCallback(it)
                }, {
                    Toast.makeText(this, "Transaksi berhasil dibuat", Toast.LENGTH_SHORT).show()
                    startActivity(DetailTransactionActivity.newIntent(this, it))
                    finish()
                })
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun doCallback(errorCode: Int) {
        when (errorCode) {
            -98 -> {
                Toast.makeText(this, "Harap isi form dengan benar", Toast.LENGTH_SHORT).show()
            }

            -99 -> {
                Toast.makeText(this, "Terjadi kesalahan, proses dibatalkan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUp() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mNewTransactionViewModel.userLiveData.postValue(getUserPreference().user)

        layout_customer.setOnClickListener {
            if (mNewTransactionViewModel.customerLiveData.value == null) {
                startActivityForResult(CustomerActivity.newIntentLookUp(this, false), CUSTOMER_LOOKUP_CODE)
            } else {
                startActivityForResult(CustomerActivity.newIntentLookUp(this, true), CUSTOMER_LOOKUP_CODE)
            }
        }

        layout_menu.setOnClickListener {
            if (mNewTransactionViewModel.menuLiveData.value == null) {
                startActivityForResult(MenuActivity.newIntentLookUp(this, false), MENU_LOOKUP_CODE)
            } else {
                startActivityForResult(MenuActivity.newIntentLookUp(this, true), MENU_LOOKUP_CODE)
            }
        }

        layout_quantity.setOnClickListener {
            if (mNewTransactionViewModel.quantityLiveData.value == null) {
                startActivityForResult(QuantityActivity.newIntent(this, 0.0), QUANTITY_LOOKUP_CODE)
            } else {
                startActivityForResult(
                    QuantityActivity.newIntent(this, mNewTransactionViewModel.quantityLiveData.value!!),
                    QUANTITY_LOOKUP_CODE
                )
            }
        }

        layout_info.setOnClickListener {
            if (mNewTransactionViewModel.infoLiveData.value.isNullOrEmpty()) {
                startActivityForResult(InfoActivity.newIntent(this, ""), INFO_LOOKUP_CODE)
            } else {
                startActivityForResult(
                    InfoActivity.newIntent(this, mNewTransactionViewModel.infoLiveData.value!!),
                    INFO_LOOKUP_CODE
                )
            }
        }
        subsribeToLiveData()
    }

    private fun subsribeToLiveData() {
        mNewTransactionViewModel.customerLiveData.observe(this, Observer {
            if (it == null) {
                txt_customer.text = "-"
            } else {
                txt_customer.text = "${it.id} - ${it.name} - ${it.phoneNumber}"
            }
        })

        mNewTransactionViewModel.menuLiveData.observe(this, Observer {
            if (it == null) {
                txt_menu.text = "-"
            } else {
                txt_menu.text = "${it.id} - ${it.name} - Rp. ${it.price}"
            }

        })

        mNewTransactionViewModel.infoLiveData.observe(this, Observer {
            if (it == null) {
                txt_info.text = "-"
            } else {
                txt_info.text = "${it}"
            }
        })


    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, NewTransactionActivity::class.java)


        const val CUSTOMER_LOOKUP_CODE = 1
        const val MENU_LOOKUP_CODE = 2
        const val QUANTITY_LOOKUP_CODE = 3
        const val INFO_LOOKUP_CODE = 4
    }
}
