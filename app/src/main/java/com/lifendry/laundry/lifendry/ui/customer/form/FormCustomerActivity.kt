package com.lifendry.laundry.lifendry.ui.customer.form

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.lifendry.laundry.lifendry.BR
import com.lifendry.laundry.lifendry.R
import com.lifendry.laundry.lifendry.base.BaseActivity
import com.lifendry.laundry.lifendry.databinding.ActivityFormCustomerBinding
import com.lifendry.laundry.lifendry.model.customer.Customer
import kotlinx.android.synthetic.main.template_toolbar.*

class FormCustomerActivity : BaseActivity<ActivityFormCustomerBinding, FormCustomerViewModel>() {

    private lateinit var mFormCustomerViewModel: FormCustomerViewModel
    private lateinit var mFormCustomerBinding: ActivityFormCustomerBinding

    override fun getLayoutId(): Int {
        return R.layout.activity_form_customer
    }

    override fun getViewModel(): FormCustomerViewModel {
        mFormCustomerViewModel = ViewModelProviders.of(this).get(FormCustomerViewModel::class.java)
        return mFormCustomerViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.formCustomerViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFormCustomerBinding = getViewDataBinding()
        setUp()
    }


    private fun setUp(){
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        if(intent.hasExtra(CUSTOMER_EXTRA)){
            mFormCustomerViewModel.setCustomer(intent.getParcelableExtra(CUSTOMER_EXTRA))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            R.id.menu_save -> {
                mFormCustomerViewModel.submit {
                    doCallback(it)
                }
                return true
            }
            else ->super.onOptionsItemSelected(item)
        }
    }

    private fun doCallback(errorCode: Int){
        when(errorCode){
            0 -> {
                Toast.makeText(this, "Aksi berhasil dilakukan", Toast.LENGTH_SHORT).show()
                if(mFormCustomerViewModel.customerIdLiveData.value != null) {
                    setResult(
                        Activity.RESULT_OK,
                        Intent().putExtra(
                            CUSTOMER_EDIT_EXTRA,
                            Customer(
                                mFormCustomerViewModel.customerIdLiveData.value,
                                mFormCustomerViewModel.customerNameLiveData.value,
                                mFormCustomerViewModel.customerEmailLiveData.value,
                                mFormCustomerViewModel.customerPhoneLiveData.value,
                                mFormCustomerViewModel.customerAddressLiveData.value
                            )
                        )
                    )
                }
                else {
                    setResult(Activity.RESULT_OK)
                }
                finish()
            }
            -99 -> {
                Toast.makeText(this, "Proses gagal, terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, FormCustomerActivity::class.java)
        fun newIntent(context: Context, customer: Customer?): Intent = Intent(context, FormCustomerActivity::class.java).apply {
            putExtra(CUSTOMER_EXTRA, customer)
        }

        const val CUSTOMER_EXTRA = "CUSTOMER_EXTRA"

        const val CUSTOMER_EDIT_EXTRA = "CUSTOMER_EDIT_EXTRA"
    }
}
