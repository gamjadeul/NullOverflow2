package com.akj.nulloverflow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.akj.nulloverflow.databinding.ActivityMainOptionBinding
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.UserState
import com.amazonaws.mobile.client.UserStateDetails
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "MainOptionTAG"

class MainOption : AppCompatActivity() {

    private val binding by lazy { ActivityMainOptionBinding.inflate(layoutInflater) }
    private var userEmail: String? = null
    private var userName: String? = null
    private val dataFormat = SimpleDateFormat("yyyy-MM-dd/hh:mm:ss", Locale.KOREA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        AWSMobileClient.getInstance().initialize(applicationContext, object: com.amazonaws.mobile.client.Callback<UserStateDetails> {
            override fun onResult(result: UserStateDetails?) {
                if(result?.userState == UserState.SIGNED_IN) {
                    userEmail = AWSMobileClient.getInstance().userAttributes["email"].toString()
                    userName = AWSMobileClient.getInstance().userAttributes["name"].toString()

                    runOnUiThread {
                        binding.userId.text = userEmail
                        binding.userName.text = userName
                    }
                }
            }

            override fun onError(e: Exception?) {
            }
        })

        //이메일 텍스트 길이가 길 수 있으므로 흐르기 효과를 주기위해
        binding.userId.isSelected = true

        //binding.userName.text = userName
        binding.seatTxt.text = intent.getStringExtra("bluetooth_name")
        val deviceAddress = intent.getStringExtra("bluetooth_address")

        //spinner에서 사용할 아이템 목록
        //해당 정보는 AWS에 반영이 되어야 함(사용목적의 변경을 위해서 필요)
        var purpose_data = listOf("스터디", "회의")
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, purpose_data)
        binding.purposeSpinner.adapter = adapter

        binding.purposeSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.purposeTxt.text = purpose_data[position]
                val updateRequest = RetrofitClient.getClient("https://gp34e91r3a.execute-api.ap-northeast-2.amazonaws.com")?.create(IRetrofit::class.java)
                val result = updateRequest?.updateInfo(deviceAddress.toString(), userEmail, 0, "unknown",
                    purpose_data[position],true, dataFormat.format(System.currentTimeMillis()))?.enqueue(object: Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    }
                })
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        //문의하기 버튼 listener
        binding.requestBtn.setOnClickListener {
            startActivity(Intent(this@MainOption, Send_email::class.java))
        }

        //액션바 상에 뒤로가기 버튼 관련된 코드, 밑에 있는 onOptionsItemSelected함수를 콜백함
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    //parameter = The menu item that was selected. This value cannot be null.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}