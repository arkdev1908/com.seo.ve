package com.seo.visualexhibition.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.seo.visualexhibition.R
import com.seo.visualexhibition.data.model.ThemeModel

class ThemeAdapter(
    private val items: List<ThemeModel>,
    private val onClick: (ThemeModel) -> Unit
) : RecyclerView.Adapter<ThemeAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgStamp: ImageView = view.findViewById(R.id.imgStamp)
        val tvYear: TextView = view.findViewById(R.id.tvYear)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_topic, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.imgStamp.setImageResource(item.imageRes)
        holder.tvYear.text = item.period

        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount(): Int = items.size
}
