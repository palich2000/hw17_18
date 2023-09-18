package com.palich.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyRecyclerViewAdapter(var items: MutableList<SuperHero>, private val onCL: (Int) -> Unit) : RecyclerView.Adapter<MyRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return MyRecyclerViewHolder(view, onCL)
    }

    override fun onBindViewHolder(holder: MyRecyclerViewHolder, position: Int) {
        holder.title.text = items[position].name
        val url = items[position].images?.sm ?: return
        Glide.with(holder.image.context).load(url).into(holder.image)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateList(list: MutableList<SuperHero>) {
        items = list
        notifyDataSetChanged()
    }
}

class MyRecyclerViewHolder(itemView: View, val onCL: (Int)->Unit) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    val title: TextView = this.itemView.findViewById(R.id.text)
    val image: ImageView = this.itemView.findViewById(R.id.image)
    init {
        itemView.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        onCL(adapterPosition)
    }
}
