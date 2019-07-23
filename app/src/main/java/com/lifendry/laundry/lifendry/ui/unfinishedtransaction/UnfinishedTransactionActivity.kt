package com.lifendry.laundry.lifendry.ui.unfinishedtransaction

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lifendry.laundry.lifendry.BR
import com.lifendry.laundry.lifendry.R
import com.lifendry.laundry.lifendry.base.BaseActivity
import com.lifendry.laundry.lifendry.databinding.ActivityUnfinishedTransactionBinding
import kotlinx.android.synthetic.main.activity_unfinished_transaction.*
import kotlinx.android.synthetic.main.template_toolbar.*

class UnfinishedTransactionActivity : BaseActivity<ActivityUnfinishedTransactionBinding, UnfinishedTransactionViewModel>() {

    private lateinit var mUnfinishedTransactionViewModel: UnfinishedTransactionViewModel
    private lateinit var mUnfinishedTransactionBinding: ActivityUnfinishedTransactionBinding
    private lateinit var mUnfinishedTransactionAdapter: UnfinishedTransactionAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_unfinished_transaction
    }

    override fun getViewModel(): UnfinishedTransactionViewModel {
        mUnfinishedTransactionViewModel = ViewModelProviders.of(this).get(UnfinishedTransactionViewModel::class.java)
        return mUnfinishedTransactionViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.unfinishedTransactionViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUnfinishedTransactionBinding = getViewDataBinding()
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

    private fun setUp(){
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        mUnfinishedTransactionAdapter = UnfinishedTransactionAdapter(listener = {

        })

        recycler_transaction.apply {
            adapter = mUnfinishedTransactionAdapter
            layoutManager = LinearLayoutManager(this@UnfinishedTransactionActivity, RecyclerView.VERTICAL, false)
        }

        mUnfinishedTransactionViewModel.setUser(getUserPreference().user)
        subscribeToLiveData()

    }

    private fun subscribeToLiveData(){
        mUnfinishedTransactionViewModel.transactionsLiveData.observe(this, Observer {
            if(it != null){
                mUnfinishedTransactionAdapter.addData(it)
            }
        })
    }

    companion object{
        fun newIntent(context: Context) : Intent = Intent(context, UnfinishedTransactionActivity::class.java)
    }
}
