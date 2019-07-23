package com.lifendry.laundry.lifendry.ui.menu

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
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
import com.lifendry.laundry.lifendry.databinding.ActivityMenuBinding
import com.lifendry.laundry.lifendry.ui.menu.detail.DetailMenuActivity
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.template_toolbar.*

class MenuActivity : BaseActivity<ActivityMenuBinding, MenuViewModel>() {

    private lateinit var mMenuBinding: ActivityMenuBinding
    private lateinit var mMenuViewModel: MenuViewModel
    private lateinit var mMenuAdapter: MenuAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_menu
    }

    override fun getViewModel(): MenuViewModel {
        mMenuViewModel = ViewModelProviders.of(this).get(MenuViewModel::class.java)
        return mMenuViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.menuViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMenuBinding = getViewDataBinding()
        setUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(intent.getBooleanExtra(MENU_EXISTS, false)){
            menuInflater.inflate(R.menu.menu_exists_cancel, menu)
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
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == MENU_LOOKUP_CODE && resultCode == RESULT_OK){
            setResult(RESULT_OK, data)
            finish()
        }
    }

    private fun setUp(){
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        mMenuAdapter = MenuAdapter(listener = {
            startActivityForResult(DetailMenuActivity.newIntentLookUp(this@MenuActivity, it), MENU_LOOKUP_CODE)
        })

        recycler_menu.apply {
            adapter = mMenuAdapter
            layoutManager = LinearLayoutManager(this@MenuActivity, RecyclerView.VERTICAL, false)
        }

        subscribeToLiveData()
    }

    private fun subscribeToLiveData(){
        mMenuViewModel.getMenusLiveData().observe(this, Observer {
            if (it != null) {
                mMenuAdapter.addData(it)
            }
        })
    }

    companion object{
        fun newIntentLookUp(context: Context, exists: Boolean): Intent = Intent(context, MenuActivity::class.java).apply {
            putExtra(MENU_LOOKUP, true)
            putExtra(MENU_EXISTS, exists)
        }
        const val MENU_LOOKUP = "MENU_LOOKUP"
        const val MENU_EXISTS = "MENU_EXISTS"

        const val MENU_LOOKUP_CODE = 1
    }
}