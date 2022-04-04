package com.akj.nulloverflow

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.akj.nulloverflow.databinding.BluetoothListBinding

class BleCustomAdapter: RecyclerView.Adapter<BleHolder>() {

    private val bleList = ArrayList<BluetoothDevice>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BleHolder {
        val binding = BluetoothListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return BleHolder(binding)
    }

    override fun onBindViewHolder(holder: BleHolder, position: Int) {
        //holder.binding.bleNameTxt.text = bleList[position].name
    }

    override fun getItemCount(): Int {
        return bleList.size
    }
}

class BleHolder(val binding: BluetoothListBinding): RecyclerView.ViewHolder(binding.root) {

}