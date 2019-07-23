package com.lifendry.laundry.lifendry.ui.menu.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lifendry.laundry.lifendry.BR
import com.lifendry.laundry.lifendry.R
import com.lifendry.laundry.lifendry.base.BaseActivity
import com.lifendry.laundry.lifendry.databinding.ActivityDetailMenuBinding
import com.lifendry.laundry.lifendry.model.menu.Menu
import kotlinx.android.synthetic.main.activity_detail_menu.*
import kotlinx.android.synthetic.main.template_toolbar.*

class DetailMenuActivity : BaseActivity<ActivityDetailMenuBinding, DetailMenuViewModel>() {

    private lateinit var mDetailMenuBinding: ActivityDetailMenuBinding
    private lateinit var mDetailMenuViewModel: DetailMenuViewModel

    override fun getLayoutId(): Int {
        return R.layout.activity_detail_menu
    }

    override fun getViewModel(): DetailMenuViewModel {
        mDetailMenuViewModel = ViewModelProviders.of(this).get(DetailMenuViewModel::class.java)
        return mDetailMenuViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.detailMenuViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDetailMenuBinding = getViewDataBinding()
        setUp()
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
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
                    Activity.RESULT_OK,
                    Intent().putExtra(
                        MENU_EXTRA,
                        mDetailMenuViewModel.getMenuLiveData().value
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
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mDetailMenuViewModel.setMenuLiveData(intent.getParcelableExtra(MENU_EXTRA))
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        mDetailMenuViewModel.getMenuLiveData().observe(this, Observer {
            if (it != null) {
                txt_menu_id.text = it.id
                txt_menu_name.text = it.name
                txt_menu_desc.text = it.desc
                txt_menu_minimum.text = it.minimum
                txt_menu_price.text = "Rp. ${it.price}"
            }
        })
    }

    companion object {
        fun newIntentLookUp(context: Context, menu: Menu): Intent =
            Intent(context, DetailMenuActivity::class.java).apply {
                putExtra(MENU_EXTRA, menu)
                putExtra(MENU_LOOKUP, true)

            }

        const val MENU_LOOKUP = "CUSTOMER_LOOKUP"
        const val MENU_EXTRA = "CUSTOMER_EXTRA"
    }
}
