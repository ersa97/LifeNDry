package com.lifendry.laundry.lifendry.ui.worker.detail

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
import com.lifendry.laundry.lifendry.databinding.ActivityDetailWorkerBinding
import com.lifendry.laundry.lifendry.model.worker.Worker
import com.lifendry.laundry.lifendry.ui.worker.form.FormWorkerActivity
import kotlinx.android.synthetic.main.activity_detail_worker.*
import kotlinx.android.synthetic.main.template_toolbar.*

class DetailWorkerActivity : BaseActivity<ActivityDetailWorkerBinding, DetailWorkerViewModel>() {

    private lateinit var mDetailWorkerViewModel: DetailWorkerViewModel
    private lateinit var mDetailWorkerBinding: ActivityDetailWorkerBinding


    override fun getLayoutId(): Int {
        return R.layout.activity_detail_worker
    }

    override fun getViewModel(): DetailWorkerViewModel {
        mDetailWorkerViewModel = ViewModelProviders.of(this).get(DetailWorkerViewModel::class.java)
        return mDetailWorkerViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.detailWorkerViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDetailWorkerBinding = getViewDataBinding()
        setUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_delete, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == WORKER_EDIT && resultCode == RESULT_OK) {
            val customer = data?.getParcelableExtra<Worker>(FormWorkerActivity.WORKER_EDIT_EXTRA)
            customer?.let {
                mDetailWorkerViewModel.setWorker(it)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            R.id.menu_edit -> {
                startActivityForResult(
                    FormWorkerActivity.newIntent(
                        this,
                        mDetailWorkerViewModel.getWorkerLiveData().value
                    ), WORKER_EDIT
                )
                true
            }
            R.id.menu_delete -> {
                mDetailWorkerViewModel.delete {
                    doCallback(it)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun doCallback(errorCode: Int) {
        when (errorCode) {
            0 -> {
                Toast.makeText(
                    this, "Pegawai berhasil dihapus",
                    Toast.LENGTH_SHORT
                ).show()
                setResult(RESULT_OK)
                finish()
            }
            -1 -> {
                Toast.makeText(
                    this, "Pegawai sudah pernah mengerjakan transaksi, proses dibatalkan",
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
        mDetailWorkerViewModel.setWorker(intent.getParcelableExtra(WORKER_EXTRA))
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        mDetailWorkerViewModel.getWorkerLiveData().observe(this, Observer {
            txt_worker_name.text = it.name
            txt_worker_id.text = it.idWorker
            txt_worker_address.text = it.address
            txt_worker_phone.text = it.phoneNumber
            txt_worker_status.text = if(it.getIsActive()) "Aktif" else "Tidak Aktif"
        })
    }

    companion object {
        fun newIntent(context: Context, worker: Worker): Intent =
            Intent(context, DetailWorkerActivity::class.java).apply {
                putExtra(WORKER_EXTRA, worker)
            }

        private const val WORKER_EXTRA = "worker_extra"
        const val WORKER_EDIT = 1
    }
}
