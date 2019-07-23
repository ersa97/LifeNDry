package com.lifendry.laundry.lifendry.ui.customer

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lifendry.laundry.lifendry.BR
import com.lifendry.laundry.lifendry.R
import com.lifendry.laundry.lifendry.base.BaseActivity
import com.lifendry.laundry.lifendry.databinding.ActivityCustomerBinding
import com.lifendry.laundry.lifendry.ui.customer.detail.DetailCustomerActivity
import com.lifendry.laundry.lifendry.ui.customer.form.FormCustomerActivity
import com.lifendry.laundry.lifendry.utils.VerticalItemDecoration
import kotlinx.android.synthetic.main.activity_customer.*
import kotlinx.android.synthetic.main.template_toolbar.*

class CustomerActivity : BaseActivity<ActivityCustomerBinding, CustomerViewModel>() {

    private lateinit var mCustomerViewModel: CustomerViewModel
    private lateinit var mCustomerBinding: ActivityCustomerBinding
    private lateinit var mCustomerAdapter: CustomerAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_customer
    }

    override fun getViewModel(): CustomerViewModel {
        mCustomerViewModel = ViewModelProviders.of(this).get(CustomerViewModel::class.java)
        return mCustomerViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.customerViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCustomerBinding = getViewDataBinding()
        setUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(intent.getBooleanExtra(CUSTOMER_EXISTS, false)){
            menuInflater.inflate(R.menu.menu_exists, menu)
        } else {
            menuInflater.inflate(R.menu.menu_add, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            R.id.menu_cancel -> {
                setResult(Activity.RESULT_OK)
                true
            }

            R.id.menu_add -> {
                startActivity(FormCustomerActivity.newIntent(this))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CUSTOMER_EDIT_DELETE && resultCode == RESULT_OK) {
            mCustomerViewModel.searchCustomer(edit_search_customer.text.toString())
        } else if(requestCode == CUSTOMER_LOOKUP_CODE && resultCode == RESULT_OK){
            setResult(RESULT_OK, data)
            finish()
        }
    }

    private fun setUp() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        mCustomerAdapter = CustomerAdapter(listener = {
            if(intent.getBooleanExtra(CUSTOMER_LOOKUP, false)){
                startActivityForResult(DetailCustomerActivity.newIntentLookUp(this, it), CUSTOMER_LOOKUP_CODE)
            } else {
                startActivityForResult(DetailCustomerActivity.newIntent(this, it), CUSTOMER_EDIT_DELETE)
            }
        })
        recycler_customer.apply {
            adapter = mCustomerAdapter
            layoutManager = LinearLayoutManager(this@CustomerActivity, RecyclerView.VERTICAL, false)
            addItemDecoration(VerticalItemDecoration(10))
        }

        btn_search.setOnClickListener {
            mCustomerViewModel.searchCustomer(edit_search_customer.text.toString())
        }
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        mCustomerViewModel.getCustomersLiveData().observe(this, Observer {
            if (it != null) {
                mCustomerAdapter.addData(it)
            }
        })
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, CustomerActivity::class.java)
        fun newIntentLookUp(context: Context, exists: Boolean): Intent = Intent(context, CustomerActivity::class.java).apply {
            putExtra(CUSTOMER_LOOKUP, true)
            putExtra(CUSTOMER_EXISTS, exists)
        }
        const val CUSTOMER_LOOKUP = "CUSTOMER_LOOKUP"
        const val CUSTOMER_EXISTS = "CUSTOMER_EXISTS"
        const val CUSTOMER_EDIT_DELETE = 1
        const val CUSTOMER_LOOKUP_CODE = 2
    }
}
