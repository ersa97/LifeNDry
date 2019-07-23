package com.lifendry.laundry.lifendry.ui.info

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import com.lifendry.laundry.lifendry.BR
import com.lifendry.laundry.lifendry.R
import com.lifendry.laundry.lifendry.base.BaseActivity
import com.lifendry.laundry.lifendry.databinding.ActivityInfoBinding
import kotlinx.android.synthetic.main.activity_main.*

class InfoActivity : BaseActivity<ActivityInfoBinding, InfoViewModel>() {

    private lateinit var mInfoViewModel: InfoViewModel
    private lateinit var mInfoBinding: ActivityInfoBinding

    override fun getLayoutId(): Int {
        return R.layout.activity_info
    }

    override fun getViewModel(): InfoViewModel {
        mInfoViewModel = ViewModelProviders.of(this).get(InfoViewModel::class.java)
        return mInfoViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.infoViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mInfoBinding = getViewDataBinding()
        setUp()
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
                setResult(
                    RESULT_OK,
                    Intent()
                        .putExtra(
                            INFO_EXTRA,
                            mInfoViewModel.infoLiveData.value
                        )
                )
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUp() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        mInfoViewModel.infoLiveData.postValue(intent.getStringExtra(INFO_EXTRA))

    }

    companion object {
        fun newIntent(context: Context, info: String): Intent = Intent(context, InfoActivity::class.java).apply {
            putExtra(INFO_EXTRA, info)
        }

        const val INFO_EXTRA = "INFO_EXTRA"
    }
}
