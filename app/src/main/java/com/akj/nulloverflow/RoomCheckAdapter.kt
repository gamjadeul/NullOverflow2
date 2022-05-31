package com.akj.nulloverflow

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akj.nulloverflow.databinding.RoomCheckBinding

class RoomCheckAdapter: RecyclerView.Adapter<RoomHolder>() {
    lateinit var roomList: BleTableData

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomHolder {
        val binding = RoomCheckBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RoomHolder(binding)
    }

    override fun getItemCount(): Int {
        return roomList.body.size
    }

    override fun onBindViewHolder(holder: RoomHolder, position: Int) {
        val room = roomList.body[position]

        holder.roomInfoTextView.text = room.location

        if(room.expireTimeMil.toLong() > System.currentTimeMillis()) {
            holder.roomStatCar.setCardBackgroundColor(Color.RED)
            holder.roomStatTx.text = "사용중"
            holder.roomStatTx.setTextColor(Color.WHITE)
        }
        else {
            holder.roomStatTx.text = "사용가능"
            holder.roomStatTx.setTextColor(Color.WHITE)
        }
        holder.setRoom(room)
    }
}

class RoomHolder(private val binding: RoomCheckBinding): RecyclerView.ViewHolder(binding.root) {

    val roomInfoTextView = binding.roomInfo
    val roomStatCar = binding.roomStatCard
    val roomStatTx = binding.roomStatTxt

    fun setRoom(room: Body?) {
        binding.roomInfo.text = room?.location
    }
}