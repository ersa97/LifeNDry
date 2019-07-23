package com.lifendry.laundry.lifendry.ui.worker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lifendry.laundry.lifendry.R
import com.lifendry.laundry.lifendry.model.worker.Worker
import kotlinx.android.synthetic.main.item_worker.view.*

class WorkerAdapter(private val list: MutableList<Worker> = mutableListOf(), private val listener: (Worker) -> Unit) : RecyclerView.Adapter<WorkerAdapter.WorkerViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerViewHolder {
        return WorkerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_worker, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: WorkerViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun addData(data: List<Worker>){
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    inner class WorkerViewHolder(private val view: View) : RecyclerView.ViewHolder(view){
        fun bind(worker: Worker){
            with(view){
                txt_id.text = worker.idWorker
                txt_name.text = worker.name
                txt_phone_number.text = worker.phoneNumber

                setOnClickListener {
                    listener(worker)
                }
            }
        }
    }
}