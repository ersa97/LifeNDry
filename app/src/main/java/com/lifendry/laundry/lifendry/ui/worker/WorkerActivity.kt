package com.lifendry.laundry.lifendry.ui.worker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lifendry.laundry.lifendry.BR
import com.lifendry.laundry.lifendry.R
import com.lifendry.laundry.lifendry.base.BaseActivity
import com.lifendry.laundry.lifendry.databinding.ActivityWorkerBinding
import com.lifendry.laundry.lifendry.prefs.UserPreference
import com.lifendry.laundry.lifendry.ui.worker.detail.DetailWorkerActivity
import com.lifendry.laundry.lifendry.ui.worker.form.FormWorkerActivity
import com.lifendry.laundry.lifendry.utils.VerticalItemDecoration
import kotlinx.android.synthetic.main.activity_worker.*
import kotlinx.android.synthetic.main.template_toolbar.*

class WorkerActivity : BaseActivity<ActivityWorkerBinding, WorkerViewModel>() {

    private lateinit var mWorkerBinding: ActivityWorkerBinding
    private lateinit var mWorkerViewModel: WorkerViewModel
    private lateinit var mWorkerAdapter: WorkerAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_worker
    }

    override fun getViewModel(): WorkerViewModel {
        mWorkerViewModel = ViewModelProviders.of(this).get(WorkerViewModel::class.java)
        return mWorkerViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.workerViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mWorkerBinding = getViewDataBinding()
        setUp()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            R.id.menu_add -> {
                startActivity(FormWorkerActivity.newIntent(this))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == WORKER_EDIT_DELETE && resultCode == RESULT_OK) {
            mWorkerViewModel.searchWorker(edit_search_worker.text.toString().toLowerCase())
        } else if(requestCode == WORKER_LOOKUP_CODE && resultCode == Activity.RESULT_OK){
            setResult(RESULT_OK, data)
            finish()
        }
    }

    private fun setUp() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        mWorkerAdapter = WorkerAdapter(listener = {
            if(intent.getBooleanExtra(WORKER_LOOKUP, false)){
                startActivityForResult(DetailWorkerActivity.newIntentLookUpActivities(this, it, intent.getStringExtra(WORKER_LOOKUP_LAUNDRY_EXTRA)), WORKER_LOOKUP_CODE)
            } else {
                startActivityForResult(DetailWorkerActivity.newIntent(this, it), WORKER_EDIT_DELETE)
            }

        })
        recycler_worker.apply {
            adapter = mWorkerAdapter
            layoutManager = LinearLayoutManager(this@WorkerActivity, RecyclerView.VERTICAL, false)
            addItemDecoration(VerticalItemDecoration(15))
        }

        btn_search.setOnClickListener {
            mWorkerViewModel.searchWorker(edit_search_worker.text.toString().toLowerCase())
        }

        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        mWorkerViewModel.setUser(getUserPreference().user)
        mWorkerViewModel.getWorkersLiveData().observe(this, Observer {
            if (it != null) {
                mWorkerAdapter.addData(it)
            }
        })

        mWorkerViewModel.getErrorLiveData().observe(this, Observer {
            if(it != null){
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object{
        fun newIntent(context: Context): Intent = Intent(context, WorkerActivity::class.java)
        fun newIntentLookUpActivities(context: Context, idLaundryActivity: String?): Intent = Intent(context, WorkerActivity::class.java).apply {
            putExtra(WORKER_LOOKUP, true)
            putExtra(WORKER_LOOKUP_LAUNDRY_EXTRA, idLaundryActivity)
        }

        const val WORKER_EDIT_DELETE = 1
        const val WORKER_LOOKUP = "WORKER_LOOKUP"
        const val WORKER_LOOKUP_LAUNDRY_EXTRA = "WORKER_LOOKUP_LAUNDRY_EXTRA"
        const val WORKER_LOOKUP_CODE = 2
    }

}
