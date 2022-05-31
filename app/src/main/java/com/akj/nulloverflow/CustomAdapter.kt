package com.akj.nulloverflow

import android.content.Intent
import android.graphics.Color
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
        val info = listData[position]

        holder.setInfo(info)

        if(listData[position].expTime > System.currentTimeMillis()){
            holder.card.setCardBackgroundColor(Color.GRAY)
            holder.fText.setTextColor(Color.BLACK)
            holder.th.setTextColor(Color.BLACK)
            holder.loc.setTextColor(Color.BLACK)
        }

        holder.itemView.setOnClickListener {
            val emptyRoomIntent = Intent(holder.itemView.context, Empty_room::class.java)
            emptyRoomIntent.putExtra("room_location", listData[position].floor.toString() + "층 " + listData[position].point)

            val occupiedRoomIntent = Intent(holder.itemView.context, Occupied_room::class.java)
            occupiedRoomIntent.putExtra("room_location", listData[position].floor.toString() + "층 " + listData[position].point)
            occupiedRoomIntent.putExtra("room_purpose", listData[position].pur)

            //현재 사용중인 자리일 경우
            if(listData[position].expTime > System.currentTimeMillis()){
                ContextCompat.startActivity(holder.itemView.context, occupiedRoomIntent, null)
            }
            else{
                ContextCompat.startActivity(holder.itemView.context, emptyRoomIntent, null)
            }
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }
}

class Holder(val binding: SeatRecylcerBinding):RecyclerView.ViewHolder(binding.root) {
    //리사이클러뷰 카드뷰
    val card = binding.seatCard
    //'1'층 '2'층 할 때 '1', '2' 등의 숫자를 나타내는 text
    val fText = binding.floorTxt
    //1'층' 2'층' 할 때 '층'을 나타내는 text
    val th = binding.floorTitle
    //연구실 테라스, 계단쪽 테라스 등 위치를 나타내는 text
    val loc = binding.point

    //정보 초기화해주는 함수
    fun setInfo(info: Info) {
        binding.floorTxt.text = "${info.floor}"
        binding.point.text = info.point
    }
}