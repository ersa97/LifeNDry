package com.lifendry.laundry.lifendry.ui.main.branchmenu


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.lifendry.laundry.lifendry.BR

import com.lifendry.laundry.lifendry.R
import com.lifendry.laundry.lifendry.base.BaseFragment
import com.lifendry.laundry.lifendry.databinding.FragmentBranchMenuBinding
import com.lifendry.laundry.lifendry.ui.historytransaction.HistoryTransactionActivity
import com.lifendry.laundry.lifendry.ui.unfinishedtransaction.UnfinishedTransactionActivity
import com.lifendry.laundry.lifendry.ui.worker.WorkerActivity
import kotlinx.android.synthetic.main.fragment_branch_menu.*

class BranchMenuFragment : BaseFragment<FragmentBranchMenuBinding, BranchMenuViewModel>() {

    private lateinit var mBranchMenuViewModel: BranchMenuViewModel
    private lateinit var mBranchMenuBinding: FragmentBranchMenuBinding

    override fun getLayoutId(): Int {
        return R.layout.fragment_branch_menu
    }

    override fun getViewModel(): BranchMenuViewModel{
        mBranchMenuViewModel = ViewModelProviders.of(this).get(BranchMenuViewModel::class.java)
        return mBranchMenuViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.branchMenuViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBranchMenuBinding = getViewDataBinding()
        setUp()
    }

    private fun setUp(){
        btn_worker.setOnClickListener {
            startActivity(WorkerActivity.newIntent(requireContext()))
        }

        btn_unfinished_transaction.setOnClickListener {
            startActivity(UnfinishedTransactionActivity.newIntent(requireContext()))
        }

        btn_history_transaction.setOnClickListener {
            startActivity(HistoryTransactionActivity.newIntent(requireContext()))
        }
    }
}
