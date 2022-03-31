package com.akj.nulloverflow

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.akj.nulloverflow.databinding.ActivityBluetoothScanningBinding

class bluetooth_scanning : AppCompatActivity() {

    val binding by lazy { ActivityBluetoothScanningBinding.inflate(layoutInflater) }

    //블루투스 아답터 가져옴
    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}