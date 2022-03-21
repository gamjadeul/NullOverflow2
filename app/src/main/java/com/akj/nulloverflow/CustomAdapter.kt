package com.akj.nulloverflow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akj.nulloverflow.databinding.SeatRecylcerBinding

class CustomAdapter: RecyclerView.Adapter<Holder>() {

    var listData = mutableListOf<Info>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = SeatRecylcerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val info = listData.get(position)
        holder.setInfo(info)
    }

    override fun getItemCount(): Int {
        return listData.size
    }
}

class Holder(val binding: SeatRecylcerBinding):RecyclerView.ViewHolder(binding.root) {
    fun setInfo(info: Info) {
        binding.floorTxt.text = "${info.floor}"
        binding.point.text = info.point
    }
}