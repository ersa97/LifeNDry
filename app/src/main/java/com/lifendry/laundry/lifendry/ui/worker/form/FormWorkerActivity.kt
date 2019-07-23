package com.lifendry.laundry.lifendry.ui.worker.form

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.lifendry.laundry.lifendry.BR
import com.lifendry.laundry.lifendry.R
import com.lifendry.laundry.lifendry.base.BaseActivity
import com.lifendry.laundry.lifendry.databinding.ActivityFormWorkerBinding
import com.lifendry.laundry.lifendry.model.worker.Worker
import kotlinx.android.synthetic.main.template_toolbar.*

class FormWorkerActivity : BaseActivity<ActivityFormWorkerBinding, FormWorkerViewModel>() {

    private lateinit var mFormWorkerViewModel: FormWorkerViewModel
    private lateinit var mFormWorkerBinding: ActivityFormWorkerBinding
    private lateinit var mWorker: Worker

    override fun getLayoutId(): Int {
        return R.layout.activity_form_worker
    }

    override fun getViewModel(): FormWorkerViewModel {
        mFormWorkerViewModel = ViewModelProviders.of(this).get(FormWorkerViewModel::class.java)
        return mFormWorkerViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.formWorkerViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFormWorkerBinding = getViewDataBinding()
        setUp()

    }

    private fun setUp() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        if (intent.hasExtra(WORKER_EXTRA)) {
            mWorker = intent.getParcelableExtra(WORKER_EXTRA)
            mFormWorkerViewModel.setWorker(mWorker)
        }
        mFormWorkerViewModel.setUser(getUserPreference().user)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.menu_save -> {
                mFormWorkerViewModel.submit {
                    doCallback(it)
                }
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun doCallback(errorCode: Int) {
        when (errorCode) {
            0 -> {
                Toast.makeText(this, "Aksi berhasil dilakukan", Toast.LENGTH_SHORT).show()
                if (mFormWorkerViewModel.workerIdLiveData.value != null) {
                    setResult(
                        Activity.RESULT_OK,
                        Intent().putExtra(
                            WORKER_EDIT_EXTRA,
                            Worker(
                                mFormWorkerViewModel.workerIdLiveData.value,
                                mFormWorkerViewModel.workerNameLiveData.value,
                                mFormWorkerViewModel.workerAddressLiveData.value,
                                mFormWorkerViewModel.workerPhoneLiveData.value,
                                mWorker.idBranch,
                                mWorker.isActive
                            )
                        )
                    )
                } else {
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
        fun newIntent(context: Context): Intent = Intent(context, FormWorkerActivity::class.java)
        fun newIntent(context: Context, worker: Worker?): Intent =
            Intent(context, FormWorkerActivity::class.java).apply {
                putExtra(WORKER_EXTRA, worker)
            }

        const val WORKER_EDIT_EXTRA = "WORKER_EDIT_EXTRA"
        const val WORKER_EXTRA = "WORKER_EXTRA"
    }
}