package com.akj.nulloverflow

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
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
        val info = listData[position]

        holder.setInfo(info)

        //현재 사용중인 자리일경우 색상이 달라야됨
        if(listData[position].uses){
            holder.card.setCardBackgroundColor(Color.GRAY)
            holder.fText.setTextColor(Color.BLACK)
            holder.th.setTextColor(Color.BLACK)
            holder.loc.setTextColor(Color.BLACK)
        }
        holder.itemView.setOnClickListener {
            val emptyRoomIntent = Intent(holder.itemView.context, Empty_room::class.java)
            val occupiedRoomIntent = Intent(holder.itemView.context, Occupied_room::class.java)
            //현재 사용중인 자리일 경우
            if(listData[position].uses){
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