package com.akj.nulloverflow

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.akj.nulloverflow.databinding.SeatRecylcerBinding

/*
    여기서 MAC주소 활용하는거 써야될 듯, MAC주소 비교해서 사용중이면 BackGround바뀌도록 -> 이거는 onCreate하든가 없음, MainActivity에서 해준다음에 정보 넘겨줘야 되나?
    또한 Click이벤트가 여기에 있으니 MAC주소로 통신해서 사용중인지 아닌지에 따라 어느 화면을 띄워줄 지
    MAC주소 전체 Scan해와서 JSON추출 후 MAC주소에 해당하는게 true면 background변경해줌
 */
class CustomAdapter: RecyclerView.Adapter<Holder>() {

    var listData = mutableListOf<Info>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = SeatRecylcerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val info = listData.get(position)
        holder.setInfo(info)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, Empty_room::class.java)
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