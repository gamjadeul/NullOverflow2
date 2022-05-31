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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class BluetoothService(private val context: Context, private var bluetoothGatt: BluetoothGatt?, private val purpose: String, private val userEmail: String) {
    //처음 생성자에 의해 생성될 때 device의 값은 null이며 이후 gatt라는 함수에서 할당됨
    private var device: BluetoothDevice? = null

    private val dataFormat = SimpleDateFormat("yyyy-MM-dd/k:mm:ss", Locale.KOREA)

    private val iterHandler = Handler(Looper.getMainLooper())

    //GATT연결에 사용될 GATT callback함수
    private val gattCallback : BluetoothGattCallback = object : BluetoothGattCallback() {
        //Callback indicating when GATT client has connected/disconnected to/from a remote GATT server.
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), bluetooth_scanning.BLUETOOTH_SCAN_PERMISSION)
            }

            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    val updateRequest = RetrofitClient.getClient("https://gp34e91r3a.execute-api.ap-northeast-2.amazonaws.com")?.create(IRetrofit::class.java)

                    val result = updateRequest?.updateInfo(device?.address.toString(), userEmail, System.currentTimeMillis() + 1800000,
                        "unknown", purpose,true, dataFormat.format(System.currentTimeMillis()))
                        ?.enqueue(object: Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                        }
                    })

                    bluetoothGatt?.discoverServices()

                    iterSend()
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    //연결이 끊겼을 때 연결이 끊겼다는 정보를 보내주고 disconnect 해주면 됨
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
            when (status) {
                //gatt 연결이 성공적으로 이루어 졌을 경우
                BluetoothGatt.GATT_SUCCESS -> {
                    handleToast(device?.name + "에 연결 성공")

                }
                else -> {
                    handleToast(device?.name + "에 연결 실패")
                }
            }
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

    private fun iterSend() {
        iterHandler.postDelayed(::sendBleInfo, 1800000)
    }

    private fun sendBleInfo() {
        val updateRequest = RetrofitClient.getClient("https://gp34e91r3a.execute-api.ap-northeast-2.amazonaws.com")?.create(IRetrofit::class.java)

        val result = updateRequest?.updateInfo(device?.address.toString(), userEmail, System.currentTimeMillis() + 1800000,
            "unknown", purpose,true, dataFormat.format(System.currentTimeMillis()))
            ?.enqueue(object: Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                }
            })

        iterSend()
    }
    //Nullable 언어로 생성자를 만든 경우 똑같이 Nullable을 리턴해줘야 오류가 나지 않음
    internal fun gatt(device: BluetoothDevice?): BluetoothGatt? {
        //역시 GATT관련 기능 사용하기 위해서는 permission check 필요(S버전 이상을 target으로 잡고있는 경우)
        //For apps targeting Build.VERSION_CODES#S or or higher, this requires the Manifest.permission#BLUETOOTH_CONNECT permission -> 버전 체크하는 코드 있어야될 듯?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), bluetooth_scanning.BLUETOOTH_SCAN_PERMISSION)
        }

        this.device = device

        //gatt를 사용하는데 존재하는 133번 요류 -> API_LEVEL 23이상에서 사용되는 함수를 사용해야하므로 사용하는 조건문
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bluetoothGatt = device?.connectGatt(context, false, gattCallback, BluetoothDevice.TRANSPORT_LE)
        } else {
            //Log.i(TAG, "BluetoothService.gatt is called (lower than VERSION_CODE M(sdk23))")
            bluetoothGatt = device?.connectGatt(context,false, gattCallback)
        }
        return bluetoothGatt
    }

    //연결이 끊기게 되면 GATT 서버와의 통신을 종료해야하는데, 이 기능을 해주는 함수
    internal fun disconnect() {
        //targetSdk가 안드로이드 S(API Lever 31)버전보다 높은 경우 필요함
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), bluetooth_scanning.BLUETOOTH_SCAN_PERMISSION)
        }

        if(bluetoothGatt != null) {
            bluetoothGatt?.disconnect()
            bluetoothGatt?.close()
            bluetoothGatt = null
            if(bluetoothGatt == null) {

                val updateRequest = RetrofitClient.getClient("https://gp34e91r3a.execute-api.ap-northeast-2.amazonaws.com")?.create(IRetrofit::class.java)
                val result = updateRequest?.updateInfo(device?.address.toString(), userEmail, System.currentTimeMillis(),
                    "unknown", "",false, dataFormat.format(System.currentTimeMillis()))
                    ?.enqueue(object: Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                        }
                    })
                handleToast("블루투스 연결해제 완료")
            }
        }
    }
}