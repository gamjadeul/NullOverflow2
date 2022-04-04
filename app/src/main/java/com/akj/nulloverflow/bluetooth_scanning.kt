package com.akj.nulloverflow

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.akj.nulloverflow.databinding.ActivityBluetoothScanningBinding

class bluetooth_scanning : AppCompatActivity() {

    val binding by lazy { ActivityBluetoothScanningBinding.inflate(layoutInflater) }
    //startActiviyForResult가 deprecated되었기 때문에 필요한 변수
    private lateinit var resultActivity: ActivityResultLauncher<Intent>

    //블루투스 아답터 가져옴
    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        //선언한 bluetootAdapter에 가져온 아답터 할당.
        bluetoothManager.adapter
    }

    //onRequestPermissionResult의 인자로 넘어갈 상수들
    private val LOCATION_PERMISSION = 100
    private val BLUETOOTH_SCAN_PERMISSION = 101

    //Scan 하는 시간
    private val SCAN_PERIOD: Long = 10000
    //Scan한 블루투스 기기를 저장할 array list
    private var deviceList = ArrayList<BluetoothDevice>()
    //scan상태를 알려주는 변수
    private var scan_state:Boolean = false
    //to schedule messages and runnables to be executed at some point in the future, 특정시간 이후에 스캔을 멈추는 동작을 하기위해 설정
    private val handler = Handler(Looper.getMainLooper())
    //블루투스 기기를 scan할 때 불러주는 startScan 및 stopScan 메서드에서 인자로 넘겨주어야할 클래스(콜백)
    private val bleScanCallBack = object : ScanCallback() {
        //BLE의 advertisement가 발견되었을 때 호출됨(필터 없이 호출되는 경우, 필터가 있어도 호출되는 경우가 존재)
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)

            result?.let {
                if(!deviceList.contains(result.device)){
                    deviceList.add(result.device)
                }
            }
        }

        //Batch scan result가 전달될 때 call back됨(lowpower옵션을 주거나 필터를 적용했을 때 호출됨, 한번에 하나의 값에만 반응을 하는 것이 아닌 전체를 묶어서 뿌려줌)
        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)

            //결과로 받은 result가 null이 아닐때
            //results의 타입 = ScanResult for Bluetooth LE scan. BluetoothDevice타입과는 다르므로 results에서 해당하는 타입을 추출해야함
            results?.let {
                for(result in it) {
                    //기존 deviceList에 없을 경우 추가
                    if(!deviceList.contains(result.device))
                        deviceList.add(result.device)
                }
            }
        }

        //scan 실패했을 때 호출되는 call back methods
        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Toast.makeText(this@bluetooth_scanning, "검색에 실패했습니다. Error : ${errorCode}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //위치권한 검사, 권한 없으면 권한 요청
        if(!checkPermission(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION)
        }

        //scanBtn에 setOnCheckedChangeListener달아서 scan할 수 있도록
        binding.scanBtn.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                //스캔 시작을 하는 부분 블루투스 사용 가능한지 불가능한지 띄워줘야함
                //블루투스가 사용 불가능한 상태라면 사용 가능하게 바꿔줘야 함
                if(bluetoothAdapter?.isEnabled == false) {
                    resultActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                        if(it.resultCode == RESULT_OK) {
                            //요청 성공
                        }
                        else {
                            //요청 실패
                        }
                    }
                    //블루투스 설정하는 System Activity를 보여줌
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    resultActivity.launch(enableBtIntent)
                }
                //블루투스 사용 확인 부분 끝

                //스캔 시작
                deviceScan(true)

            } else {
                //스캔을 멈추는 부분
                deviceScan(false)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        //블루투스를 지원하는지 확인해야함, 확인 후 지원하지 않는다면 앱이 종료되도록 설정 -> 이후 블루투스 지원여부 분기문을 작성 안해도 됨
        if(!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE기능을 지원하지 않는 기기입니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    //permission확인해주는 함수
    fun checkPermission(permission_list: Array<String>): Boolean {
        //bluetoothAdapter.bluetoothLeScanner.startScan() 매서드가 Marshmallow이상 버전에서만 사용이 가능함
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for(permission in permission_list) {
                //해당 권한이 없을 경우 false반환
                if(ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }

    //사용자 요청을 처리할때 콜백되는 함수 Override
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            LOCATION_PERMISSION -> {
                //요청정보 배열에서 권한이 없다면 종료 -> 위치권한 없는건 종료까진 필요없을라나
                if(grantResults.isNotEmpty()) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "해당 권한을 승인해야합니다.", Toast.LENGTH_SHORT).show()
                        //finish()
                    }
                }
            }
            BLUETOOTH_SCAN_PERMISSION -> {
                if(grantResults.isNotEmpty()) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "해당 권한을 승인해야합니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    //API레벨이 21이상인 LOLLIPOP버전 이상만 사용 가능
    //state의 상태에 따라서 핸들러를 이용, BLE기기를 scan하도록
    //compilesdk가 32 즉, marshmallow보다 높은 버전임, 이렇게되면 권한 사용할때마다 check해줘야됨, 모든 권한을 check할 수 있는 check함수 만들어야될듯
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun deviceScan(state: Boolean) {
        //BLUETOOTH_SCAN에 관한 permission 없을 때 요청해줘여야됨, 있으면 바로 및 if(state)문으로 진입
        if (!checkPermission(arrayOf(Manifest.permission.BLUETOOTH_SCAN))) {
            //permission요청
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_SCAN), BLUETOOTH_SCAN_PERMISSION)
        }
        if(state) {
            handler.postDelayed({
                scan_state = false
                bluetoothAdapter?.bluetoothLeScanner?.stopScan(bleScanCallBack)
            }, SCAN_PERIOD)
            scan_state = true
            deviceList.clear()
            bluetoothAdapter?.bluetoothLeScanner?.startScan(bleScanCallBack)
        } else {
            scan_state = false
            bluetoothAdapter?.bluetoothLeScanner?.stopScan(bleScanCallBack)
        }
    }
}