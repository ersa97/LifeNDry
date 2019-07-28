package com.lifendry.laundry.lifendry.ui.settingserver

import android.annotation.SuppressLint
import android.app.Activity
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.lifendry.laundry.lifendry.BR
import com.lifendry.laundry.lifendry.R
import com.lifendry.laundry.lifendry.R.array.server
import com.lifendry.laundry.lifendry.base.BaseActivity
import com.lifendry.laundry.lifendry.databinding.ActivityServerSettingBinding
import com.lifendry.laundry.lifendry.model.server.Server
import kotlinx.android.synthetic.main.activity_server_setting.*
import kotlinx.android.synthetic.main.template_toolbar.*

class ServerSettingActivity : BaseActivity<ActivityServerSettingBinding, ServerSettingViewModel>() {

    private lateinit var mServerSettingViewModel: ServerSettingViewModel
    private lateinit var mServerSettingBinding: ActivityServerSettingBinding

    override fun getLayoutId(): Int {
        return R.layout.activity_server_setting
    }

    override fun getViewModel(): ServerSettingViewModel{
        mServerSettingViewModel = ViewModelProviders.of(this).get(ServerSettingViewModel::class.java)
        return mServerSettingViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.settingServerViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mServerSettingBinding = getViewDataBinding()
        setUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return when(item?.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            R.id.menu_save -> {

                getServerPreference().server = Server(idServer = spinner_server.selectedItemPosition + 1, ip = spinner_server.selectedItem.toString())

                Toast.makeText(this, "ID : ${getServerPreference().server?.idServer} === IP : ${getServerPreference().server?.ip}", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
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
        ArrayAdapter.createFromResource(
            this,
            server,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_server.adapter = adapter
        }

        if(getServerPreference().server != null){
            getServerPreference()?.server?.idServer?.let { spinner_server.setSelection(it-1) }
        }

    }

}
