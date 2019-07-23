package com.lifendry.laundry.lifendry.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import com.lifendry.laundry.lifendry.BR
import com.lifendry.laundry.lifendry.R
import com.lifendry.laundry.lifendry.base.BaseActivity
import com.lifendry.laundry.lifendry.databinding.ActivityMainBinding
import com.lifendry.laundry.lifendry.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    private lateinit var mMainViewModel: MainViewModel
    private lateinit var mMainBinding: ActivityMainBinding

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getViewModel(): MainViewModel {
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        return mMainViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.mainViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMainBinding = getViewDataBinding()
        setUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_logout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            R.id.menu_logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        getUserPreference().user = null
        startActivity(LoginActivity.newIntent(this))
        finish()
    }

    private fun setUp() {
        setSupportActionBar(toolbar)

        val pagerAdapter = MainPagerAdapter(supportFragmentManager, this)
        viewpager.adapter = pagerAdapter
        tablayout.setupWithViewPager(viewpager)
    }


    companion object {
        fun newIntent(context: Context): Intent = Intent(context, MainActivity::class.java)
    }
}
