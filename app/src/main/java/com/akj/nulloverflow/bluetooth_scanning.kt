package com.akj.nulloverflow

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

    //Scan 하는 시간
    private val SCAN_PERIOD: Long = 10000
    //Scan한 블루투스 기기를 저장할 array list
    private var deviceList = ArrayList<BluetoothDevice>()
    //scan상태를 알려주는 변수
    private var scan_state:Boolean = false

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

        //위치정보 사용권한 확인
        checkPermission()

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

            } else {
                //스캔을 멈추는 부분
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
    fun checkPermission() {
        //ACCESS_FINE_LOCATION이나 ACCESS_COARSE_LOCATION에 관한 권한이 하나라도 GRANTED되어있지 않으면 권한을 요청한다.
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            //3번재 parameter인 100은 구분을 위해서 넣어주는 requestCode임
            ActivityCompat.requestPermissions(this, permissions, 100)
        } else {
            //모든 권한이 있을 떄 이부분에서 호출 발생 -> 딱히 할 행동 없으므로 안적어도 될듯
        }
    }

    //기기를 scan할 때 사용할 함수 만들어 줘야됨

    //사용자 요청을 처리할때 콜백되는 함수 Override
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            100 -> {
                //요청정보 배열을 돌면서 확인
                if(grantResults.isNotEmpty()) {
                    for (grant in grantResults) {
                        if (grant != PackageManager.PERMISSION_GRANTED)
                            Toast.makeText(this, "위치정보 사용을 거절하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    //API레벨이 21이상인 LOLLIPOP버전 이상만 사용 가능
    //state의 상태에 따라서 핸들러를 이용, BLE기기를 scan하도록
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun deviceScan(state: Boolean) {
        if(!state) {

        } else {

        }
    }
}