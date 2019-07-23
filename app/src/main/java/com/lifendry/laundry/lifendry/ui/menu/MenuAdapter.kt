package com.lifendry.laundry.lifendry.ui.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lifendry.laundry.lifendry.R
import com.lifendry.laundry.lifendry.model.menu.Menu
import kotlinx.android.synthetic.main.item_menu.view.*

class MenuAdapter(private val list: MutableList<Menu> = mutableListOf(), private val listener: (Menu) -> Unit) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_menu, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun addData(data: List<Menu>){
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    inner class MenuViewHolder(private val view: View) : RecyclerView.ViewHolder(view){
        fun bind(menu: Menu){
            with(view){
                txt_id.text = menu.id
                txt_name.text = menu.name
                txt_price.text = "Rp. ${menu.price}"
                txt_minimum.text = menu.minimum

                setOnClickListener {
                    listener(menu)
                }
            }
        }
    }
}
