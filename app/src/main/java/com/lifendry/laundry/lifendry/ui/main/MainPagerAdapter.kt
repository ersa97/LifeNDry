package com.lifendry.laundry.lifendry.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.lifendry.laundry.lifendry.R
import com.lifendry.laundry.lifendry.ui.main.branchmenu.BranchMenuFragment
import com.lifendry.laundry.lifendry.ui.main.transaction.TransactionFragment

class MainPagerAdapter(fragmentManager: FragmentManager, val context: Context) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> TransactionFragment()
            else -> BranchMenuFragment()
        }
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> context.getString(R.string.transaksi)
            else -> context.getString(R.string.cabang)
        }
    }
}