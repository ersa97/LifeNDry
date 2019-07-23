package com.lifendry.laundry.lifendry.ui.customer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lifendry.laundry.lifendry.R
import com.lifendry.laundry.lifendry.model.customer.Customer
import kotlinx.android.synthetic.main.item_customer.view.*

class CustomerAdapter(private val list: MutableList<Customer> = mutableListOf(), private val listener: (Customer) -> Unit) : RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        return CustomerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_customer, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun addData(data: List<Customer>){
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    inner class CustomerViewHolder(private val view: View) : RecyclerView.ViewHolder(view){
        fun bind(customer: Customer){
            with(view){
                txt_email.text = customer.email
                txt_name.text = customer.name
                txt_phone_number.text = customer.phoneNumber

                setOnClickListener {
                    listener(customer)
                }
            }
        }
    }
}