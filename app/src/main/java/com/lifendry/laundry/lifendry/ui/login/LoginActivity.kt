package com.lifendry.laundry.lifendry.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.lifendry.laundry.lifendry.BR
import com.lifendry.laundry.lifendry.R
import com.lifendry.laundry.lifendry.base.BaseActivity
import com.lifendry.laundry.lifendry.databinding.ActivityLoginBinding
import com.lifendry.laundry.lifendry.prefs.UserPreference
import com.lifendry.laundry.lifendry.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.template_toolbar.*

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    lateinit var mLoginViewModel: LoginViewModel
    lateinit var mLoginBinding: ActivityLoginBinding

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun getViewModel(): LoginViewModel {
        mLoginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        return mLoginViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.loginViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkLogin()
        mLoginBinding = getViewDataBinding()
        setUp()
    }

    private fun setUp(){
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btn_login.setOnClickListener {
            mLoginViewModel.login()
        }

        subscribeToLiveData()
    }

    private fun subscribeToLiveData(){
        mLoginViewModel.getUserLiveData().observe(this, Observer {
            if(it != null){
                getUserPreference().user = it
                checkLogin()
            }
        })

        mLoginViewModel.getErrorLoginLiveData().observe(this, Observer {
            if(!it.isNullOrEmpty()){
                Snackbar.make(mLoginBinding.root, it, Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkLogin(){
        if(getUserPreference().user!= null){
            if(getUserPreference().user?.roleId == 0){
                startActivity(MainActivity.newIntent(this))
                finish()
            } else {
                Snackbar.make(mLoginBinding.root, "Username atau password salah", Snackbar.LENGTH_SHORT).show()
                getUserPreference().user = null
            }
        }
    }

    companion object{
        fun newIntent(context: Context) : Intent = Intent(context, LoginActivity::class.java)
    }
}
