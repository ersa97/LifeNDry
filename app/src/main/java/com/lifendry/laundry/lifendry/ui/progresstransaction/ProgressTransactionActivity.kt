package com.lifendry.laundry.lifendry.ui.progresstransaction

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lifendry.laundry.lifendry.BR
import com.lifendry.laundry.lifendry.R
import com.lifendry.laundry.lifendry.base.BaseActivity
import com.lifendry.laundry.lifendry.databinding.ActivityProgressTransactionBinding
import com.lifendry.laundry.lifendry.model.worker.Worker
import com.lifendry.laundry.lifendry.ui.worker.WorkerActivity
import com.lifendry.laundry.lifendry.ui.worker.detail.DetailWorkerActivity
import com.lifendry.laundry.lifendry.utils.VerticalItemDecoration
import kotlinx.android.synthetic.main.activity_progress_transaction.*
import kotlinx.android.synthetic.main.template_toolbar.*

class ProgressTransactionActivity : BaseActivity<ActivityProgressTransactionBinding, ProgressTransactionViewModel>() {

    private lateinit var mProgressTransactionViewModel: ProgressTransactionViewModel
    private lateinit var mProgressTransactionBinding: ActivityProgressTransactionBinding
    private lateinit var mProgressTransactionAdapter: ProgressTransactionAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_progress_transaction
    }

    override fun getViewModel(): ProgressTransactionViewModel {
        mProgressTransactionViewModel = ViewModelProviders.of(this).get(ProgressTransactionViewModel::class.java)
        return mProgressTransactionViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.progressTransactionViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mProgressTransactionBinding = getViewDataBinding()
        setUp()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == WORKER_LOOKUP_CODE && resultCode == RESULT_OK) {
            if (data?.hasExtra(DetailWorkerActivity.WORKER_EXTRA) == true && data.hasExtra(WorkerActivity.WORKER_LOOKUP_LAUNDRY_EXTRA)) {
                val worker = data.getParcelableExtra<Worker>(DetailWorkerActivity.WORKER_EXTRA)
                val id = data.getStringExtra(WorkerActivity.WORKER_LOOKUP_LAUNDRY_EXTRA)
                mProgressTransactionViewModel.doActivity(id, worker.idWorker, {
                    Toast.makeText(this, "Aktivitas laundry berhasil dikerjakan", Toast.LENGTH_SHORT).show()
                    mProgressTransactionViewModel.load()
                }, {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                })
            }
        }
    }

    private fun setUp() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        mProgressTransactionViewModel.setServerLiveData(getServerPreference().server)

        mProgressTransactionAdapter = ProgressTransactionAdapter(startListener = {
            startActivityForResult(
                WorkerActivity.newIntentLookUpActivities(this, it.idActivityLaundry),
                WORKER_LOOKUP_CODE
            )
        }, finishListener = {
            mProgressTransactionViewModel.finishActivity(it.idActivityLaundry, {
                Toast.makeText(this, "Aktivitas laundry berhasil diselesaikan", Toast.LENGTH_SHORT).show()
                mProgressTransactionViewModel.load()
                setResult(Activity.RESULT_OK)
            }, {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            })
        })

        recycler_progress.apply {
            adapter = mProgressTransactionAdapter
            layoutManager = LinearLayoutManager(this@ProgressTransactionActivity, RecyclerView.VERTICAL, false)
            addItemDecoration(VerticalItemDecoration(15))
        }

        mProgressTransactionViewModel.setLaundryId(intent.getStringExtra(LAUNDRY_ID_EXTRA))

        subscribeToLiveData()

    }

    private fun subscribeToLiveData() {
        mProgressTransactionViewModel.activityLaundryLiveData.observe(this, Observer {
            if (it != null) {
                mProgressTransactionAdapter.setData(it)
            }
        })

        mProgressTransactionViewModel.laundryIdLiveData.observe(this, Observer {
            if (it != null) {
                txt_transaction_id.text = it
            }
        })
    }


    companion object {
        fun newIntent(context: Context, idLaundry: String?): Intent =
            Intent(context, ProgressTransactionActivity::class.java).apply {
                putExtra(LAUNDRY_ID_EXTRA, idLaundry)
            }

        const val LAUNDRY_ID_EXTRA = "LAUNDRY_ID_EXTRA"
        const val WORKER_LOOKUP_CODE = 1
    }
}


