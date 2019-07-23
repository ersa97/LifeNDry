package com.lifendry.laundry.lifendry.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.lifendry.laundry.lifendry.prefs.UserPreference

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel> : Fragment() {
    private lateinit var mViewDataBinding: T
    private lateinit var mViewModel: V
    private lateinit var rootView: View
    private var mBaseActivity: BaseActivity<*, *>? = null

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getViewModel(): V

    abstract fun getBindingVariable(): Int

    fun getViewDataBinding(): T {
        return mViewDataBinding
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            mBaseActivity = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = getViewModel()
        setHasOptionsMenu(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        rootView = mViewDataBinding.root
        return rootView
    }

    override fun onDetach() {
        mBaseActivity = null
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel)
        mViewDataBinding.lifecycleOwner = viewLifecycleOwner
        mViewDataBinding.executePendingBindings()
    }

    fun getBaseActivity(): BaseActivity<*, *>? {
        return mBaseActivity
    }
}