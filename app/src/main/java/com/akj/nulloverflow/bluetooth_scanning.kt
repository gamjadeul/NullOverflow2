package com.akj.nulloverflow

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.akj.nulloverflow.databinding.ActivityBluetoothScanningBinding
import java.util.jar.Manifest

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
}