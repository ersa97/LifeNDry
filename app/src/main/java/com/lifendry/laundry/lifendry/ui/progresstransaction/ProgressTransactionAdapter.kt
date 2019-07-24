package com.lifendry.laundry.lifendry.ui.progresstransaction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lifendry.laundry.lifendry.R
import com.lifendry.laundry.lifendry.model.activitylaundry.ActivityLaundry
import com.lifendry.laundry.lifendry.model.transaction.Transaction
import kotlinx.android.synthetic.main.item_progress_transaction.view.*

class ProgressTransactionAdapter(private val list: MutableList<ActivityLaundry> = mutableListOf(), private val startListener: (ActivityLaundry) -> Unit, private val finishListener: (ActivityLaundry) -> Unit) : RecyclerView.Adapter<ProgressTransactionAdapter.ProgressTransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgressTransactionViewHolder {
        return ProgressTransactionViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_progress_transaction, parent, false)
        )
    }

    fun setData(data: List<ActivityLaundry>){
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProgressTransactionViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ProgressTransactionViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(a: ActivityLaundry){
            with(view){
                txt_activity_name.text = a.nameActivity
                txt_activity_worker.text = "Pekerja : " + if(a.idWorker.isNullOrEmpty()) "-" else "${a.idWorker} - ${a.nameWorker}"
                txt_activity_start.text = if(a.start == null) "Belum dikerjakan" else "Mulai : ${resources.getString(R.string.date_time_format, a.start)}"
                txt_activity_end.text = if(a.end == null) "Belum selesai" else "Selesai : ${resources.getString(R.string.date_time_format, a.end)}"

                btn_start.isEnabled = a.start == null
                btn_finish.isEnabled = a.start != null && a.end == null

                btn_start.setOnClickListener {
                    startListener(a)
                }

                btn_finish.setOnClickListener {
                    finishListener(a)
                }

            }
        }
    }
}