package com.lifendry.laundry.lifendry.ui.quantity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lifendry.laundry.lifendry.BR
import com.lifendry.laundry.lifendry.R
import com.lifendry.laundry.lifendry.base.BaseActivity
import com.lifendry.laundry.lifendry.databinding.ActivityQuantityBinding
import kotlinx.android.synthetic.main.activity_quantity.*
import kotlinx.android.synthetic.main.template_toolbar.*

class QuantityActivity : BaseActivity<ActivityQuantityBinding, QuantityViewModel>() {

    private lateinit var mQuantityViewModel: QuantityViewModel
    private lateinit var mQuantityBinding: ActivityQuantityBinding

    override fun getLayoutId(): Int {
        return R.layout.activity_quantity
    }

    override fun getViewModel(): QuantityViewModel {
        mQuantityViewModel = ViewModelProviders.of(this).get(QuantityViewModel::class.java)
        return mQuantityViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.quantityViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mQuantityBinding = getViewDataBinding()
        setUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            android.R.id.home -> {
                finish()
                true
            }

            R.id.menu_save -> {
                setResult(RESULT_OK,
                    Intent()
                        .putExtra(QUANTITY_EXTRA,
                            if(edit_quantity.text.toString().isEmpty()) 0.0 else edit_quantity.text.toString().toDouble())
                )
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

        mQuantityViewModel.quantityLiveData.postValue(intent.getDoubleExtra(QUANTITY_EXTRA, 0.0))

        subsribeToLiveData()

    }

    private fun subsribeToLiveData(){
        mQuantityViewModel.quantityLiveData.observe(this, Observer {
            edit_quantity.setText(it.toString())
        })
    }

    companion object{
        fun newIntent(context: Context, quantity: Double) : Intent = Intent(context, QuantityActivity::class.java).apply {
            putExtra(QUANTITY_EXTRA, quantity)
        }

        fun newIntent(context:Context): Intent = Intent(context, QuantityActivity::class.java)

        const val QUANTITY_EXTRA = "QUANTITY_EXTRA"
    }
}
