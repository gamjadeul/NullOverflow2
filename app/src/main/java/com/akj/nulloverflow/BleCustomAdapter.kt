package com.akj.nulloverflow

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.akj.nulloverflow.databinding.BluetoothListBinding

class BleCustomAdapter: RecyclerView.Adapter<BleHolder>() {

    private val bleList = ArrayList<BluetoothDevice>()
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BleHolder {
        val binding = BluetoothListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return BleHolder(binding)
    }

    override fun onBindViewHolder(holder: BleHolder, position: Int) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            //외부함수 끌어다 쓰는데 문제가 있음, 그냥 bluetooth_scanning.kt에 다 구현하는게 나을듯
        }
        holder.binding.bleNameTxt.text = bleList[position].name
        holder.binding.bleAddTxt.text = bleList[position].address
    }

    override fun getItemCount(): Int {
        return bleList.size
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        context = recyclerView.context
    }
}

class BleHolder(val binding: BluetoothListBinding): RecyclerView.ViewHolder(binding.root) {

}