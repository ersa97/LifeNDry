package com.lifendry.laundry.lifendry.ui.historytransaction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lifendry.laundry.lifendry.R
import com.lifendry.laundry.lifendry.model.menu.Menu
import com.lifendry.laundry.lifendry.model.transaction.Transaction
import kotlinx.android.synthetic.main.item_transaction.view.*

class HistoryTransactionAdapter(
    private val list: MutableList<Transaction> = mutableListOf(),
    private val listener: (Transaction) -> Unit
) : RecyclerView.Adapter<HistoryTransactionAdapter.UnfinishedTransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnfinishedTransactionViewHolder {
        return UnfinishedTransactionViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_transaction, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: UnfinishedTransactionViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun addData(data: List<Transaction>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    inner class UnfinishedTransactionViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(t: Transaction) {
            with(view) {
                txt_transaction_id.text = t.idTransaction
                txt_transaction_date.text = resources.getString(R.string.date_format, t.date)
                txt_transaction_customer.text = "${t.idCustomer} - ${t.customer}"
                txt_transaction_price.text = "Rp. ${t.price}"

                setOnClickListener {
                    listener(t)
                }
            }
        }
    }
}
