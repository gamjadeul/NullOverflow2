package com.akj.nulloverflow

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat

private val TAG = "GATTConnect"

//bluetooth_scanning 부분에서 gattCallback 구현 및 다른 기능들을 구현할 수 있지만 다른 class 파일 만들어서 관리
//bluetooth_scanning 에서 넘어오는 bluetoothGatt 값은 처음에는 null -> 계속해서 null 값임
class BluetoothService(private val context: Context, private var bluetoothGatt: BluetoothGatt?) {

    //처음 생성자에 의해 생성될 때 device의 값은 null이며 이후 gatt라는 함수에서 할당됨
    private var device: BluetoothDevice? = null
    //GATT연결에 사용될 GATT callback함수
    private val gattCallback : BluetoothGattCallback = object : BluetoothGattCallback() {

        //Callback indicating when GATT client has connected/disconnected to/from a remote GATT server.
        //연결상태 및 연결 해제 상태를 알고 있어야 하므로 필요
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), bluetooth_scanning.BLUETOOTH_SCAN_PERMISSION)
            }

            //Log.i(TAG, "onConnectionStateChange is called, status: $status")
            //Log.i(TAG, "onConnectionStateChange is called newState: $newState")
            //Log.i(TAG, "onConnectionStateChange is called device's name: ${device?.name}")
            /*
            if (bluetoothGatt == null) {
                Log.i(TAG, "bluetoothGatt is null")
            }

             */

            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    //연결되었을 때 연결정보 보내주면 됨
                    //MAC Address를 보내주는 것이 아닌 Minor 값을 꺼내서 사용해야할듯
                    //해당하는 bluetoothGatt 객체에서 제공하는 서비스를 검색하고 해당 기기에서 서비스가 가능한 목록들을 onServicesDiscovered 함수에 콜백을 시켜준다.
                    //Minor 값만 얻어오는 거면 굳이 서비스 필요 없을거 같긴한데, 연결정보랑 Minor 값 받아와서 확인하면 될거같음
                    //test
                    //Log.i(TAG, "연결상태 확인, onServiceDiscoverd 콜백")
                    bluetoothGatt?.discoverServices()
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    //연결이 끊겼을 때 연결이 끊겼다는 정보를 보내주고 disconnect 해주면 됨
                    //test
                    //Log.i(TAG, "GATT서버 연결 해제")
                    disconnect()
                }
            }
        }

        //Callback invoked when the list of remote services, characteristics and descriptors for the remote device have been updated, ie new services have been discovered.
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), bluetooth_scanning.BLUETOOTH_SCAN_PERMISSION)
            }

            //위 onConnectionStateChange 함수에서 연결상태가 되면 discoverServices()가 호출이 되는데 호출되면 해당 콜백함수를 호출하게 됨
            //각 기기마다 UUID가 있음(사용 목적에 따라서, 기기마다 제공하는 서비스가 존재함 -> 확인 후 어떤 UUID이고 어떤 서비스를 제공하는지 봐야 됨)
            //test
            when (status) {
                //gatt 연결이 성공적으로 이루어 졌을 경우
                BluetoothGatt.GATT_SUCCESS -> {
                    //test
                    //Log.i(TAG, "GATT연결 성공, status: $status")
                    handleToast(device?.name + "에 연결 성공")

                }
                else -> {
                    handleToast(device?.name + "에 연결 실패")
                }
            }
        }

        //나중에 readRemoteRssi함수 호출되면 해당 콜백함수가 호출됨
        override fun onReadRemoteRssi(gatt: BluetoothGatt?, rssi: Int, status: Int) {
            super.onReadRemoteRssi(gatt, rssi, status)
        }
    }

    //toast message test를 위한 핸들러 함수
    private fun handleToast(msg: String) {
        val handler = Handler(Looper.getMainLooper())

        //test
        handler.post {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    //Nullable 언어로 생성자를 만든 경우 똑같이 Nullable을 리턴해줘야 오류가 나지 않음
    internal fun gatt(device: BluetoothDevice): BluetoothGatt? {
        //역시 GATT관련 기능 사용하기 위해서는 permission check 필요(S버전 이상을 target으로 잡고있는 경우)
        //For apps targeting Build.VERSION_CODES#S or or higher, this requires the Manifest.permission#BLUETOOTH_CONNECT permission -> 버전 체크하는 코드 있어야될 듯?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), bluetooth_scanning.BLUETOOTH_SCAN_PERMISSION)
        }

        this.device = device

        //gatt를 사용하는데 존재하는 133번 요류 -> API_LEVEL 23이상에서 사용되는 함수를 사용해야하므로 사용하는 조건문
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //4번째 인자로 오는 값은 GATT 연결에 사용할 모드
            //Log.i(TAG, "BluetoothService.gatt is called (higher than VERSION_CODE M(sdk23))")
            //Log.i(TAG, "device is ${device.address}")
            //Log.i(TAG, "This device version is ${Build.VERSION.SDK_INT}")
            bluetoothGatt = device.connectGatt(context, false, gattCallback, BluetoothDevice.TRANSPORT_LE)
        } else {
            //Log.i(TAG, "BluetoothService.gatt is called (lower than VERSION_CODE M(sdk23))")
            bluetoothGatt = device.connectGatt(context,false, gattCallback)
        }
        return bluetoothGatt
    }

    //연결이 끊기게 되면 GATT 서버와의 통신을 종료해야하는데, 이 기능을 해주는 함수
    internal fun disconnect() {
        //targetSdk가 안드로이드 S(API Lever 31)버전보다 높은 경우 필요함
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), bluetooth_scanning.BLUETOOTH_SCAN_PERMISSION)
        }
        //Log.i(TAG, "disconnect method enter")
        //bluetoothGatt 값이 여전히 null인듯
        if(bluetoothGatt != null) {
            bluetoothGatt?.disconnect()
            bluetoothGatt?.close()
            bluetoothGatt = null
            if(bluetoothGatt == null) {
                //Log.i(TAG, "disconnect and close complete, bluetoothGatt is null")
                handleToast("블루투스 연결해제 완료")
            }
        }
    }
}