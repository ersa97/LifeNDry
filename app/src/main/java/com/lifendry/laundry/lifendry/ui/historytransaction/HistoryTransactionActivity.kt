package com.lifendry.laundry.lifendry.ui.historytransaction

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lifendry.laundry.lifendry.BR
import com.lifendry.laundry.lifendry.R
import com.lifendry.laundry.lifendry.base.BaseActivity
import com.lifendry.laundry.lifendry.databinding.ActivityHistoryTransactionBinding
import com.lifendry.laundry.lifendry.ui.detailtransaction.DetailTransactionActivity
import com.lifendry.laundry.lifendry.utils.VerticalItemDecoration
import kotlinx.android.synthetic.main.activity_history_transaction.*
import kotlinx.android.synthetic.main.template_toolbar.*

class HistoryTransactionActivity : BaseActivity<ActivityHistoryTransactionBinding, HistoryTransactionViewModel>() {

    private lateinit var mHistoryTransactionViewModel: HistoryTransactionViewModel
    private lateinit var mHistoryTransactionAdapter: HistoryTransactionAdapter
    private lateinit var mHistoryTransactionBinding: ActivityHistoryTransactionBinding

    override fun getLayoutId(): Int {
        return R.layout.activity_history_transaction
    }

    override fun getViewModel(): HistoryTransactionViewModel {
        mHistoryTransactionViewModel = ViewModelProviders.of(this).get(HistoryTransactionViewModel::class.java)
        return mHistoryTransactionViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.historyTransactionViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHistoryTransactionBinding = getViewDataBinding()
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

        mHistoryTransactionViewModel.setServerLiveData(getServerPreference().server)

        mHistoryTransactionAdapter = HistoryTransactionAdapter(listener = {
            startActivity(DetailTransactionActivity.newIntent(this, it))
        })

        recycler_transaction.apply {
            adapter = mHistoryTransactionAdapter
            layoutManager = LinearLayoutManager(this@HistoryTransactionActivity, RecyclerView.VERTICAL, false)
            addItemDecoration(VerticalItemDecoration(15))
        }

        mHistoryTransactionViewModel.setUser(getUserPreference().user)
        subscribeToLiveData()

    }

    private fun subscribeToLiveData(){
        mHistoryTransactionViewModel.transactionsLiveData.observe(this, Observer {
            if(it != null){
                mHistoryTransactionAdapter.addData(it)
            }
        })
    }

    companion object{
        fun newIntent(context: Context) : Intent = Intent(context, HistoryTransactionActivity::class.java)
    }
}
