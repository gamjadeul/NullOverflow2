package com.akj.nulloverflow

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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

        //테라스 자리 누르면 사용중인지 아닌지에 따라 화면이 변경되어야 하는데 이건 블루투스 모듈 오면 추가 해야할 듯 함
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, Occupied_room::class.java)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }
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