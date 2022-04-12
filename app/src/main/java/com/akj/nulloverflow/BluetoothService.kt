package com.akj.nulloverflow

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

//bluetooth_scanning부분에서 gattCallback구현 및 다른 기능들을 구현할 수 있지만 다른 class 파일 만들어서 관리
//bluetooth_scanning에서 넘어오는 bluetoothGatt값은 처음에는 null
class BluetoothService(private val context: Context, private var bluetoothGatt: BluetoothGatt?) {

    //처음 생성사에 의해 생성될 때 device의 값은 null이며 이후 gatt라는 함수에서 할당됨
    private var device: BluetoothDevice? = null
    //GATT연결에 사용될 GATT callback함수
    private val gattCallback : BluetoothGattCallback = object : BluetoothGattCallback() {

        //Callback indicating when GATT client has connected/disconnected to/from a remote GATT server.
        //연결상태 및 연결 해제 상태를 알고 있어야 하므로 필요
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
        }

        //Callback invoked when the list of remote services, characteristics and descriptors for the remote device have been updated, ie new services have been discovered.
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
        }
    }
    //Nullable 언어로 생성자를 만든 경우 똑같이 Nullable을 리턴해줘야 오류가 나지 않음
    fun gatt(device: BluetoothDevice): BluetoothGatt? {
        //역시 GATT관련 기능 사용하기 위해서는 permission check 필요(S버전 이상을 target으로 잡고있는 경우)
        //For apps targeting Build.VERSION_CODES#S or or higher, this requires the Manifest.permission#BLUETOOTH_CONNECT permission -> 버전 체크하는 코드 있어야될 듯?
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), bluetooth_scanning.BLUETOOTH_SCAN_PERMISSION)
        }
        //gatt를 사용하는데 존재하는 133번 요류 -> API_LEVEL 23이상에서 사용되는 함수를 사용해야하므로 사용하는 조건문
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //4번째 인자로 오는 값은 GATT 연결에 사용할 모드
            device.connectGatt(context, false, gattCallback, BluetoothDevice.TRANSPORT_LE)
        } else {
            device.connectGatt(context,false ,gattCallback)
        }
        this.device = device
        return bluetoothGatt
    }
}