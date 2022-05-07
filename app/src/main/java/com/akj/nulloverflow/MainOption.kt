package com.akj.nulloverflow

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.akj.nulloverflow.databinding.ActivityMainOptionBinding

class MainOption : AppCompatActivity() {

    private val binding by lazy { ActivityMainOptionBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        /*
        이 부분은 옵션창에서 이름이랑 ID 받아와서 적용시켜주는 부분
        var user_id = binding.userId.setText()
        var user_name = binding.userName.setText()
         */

        binding.seatTxt.text = intent.getStringExtra("bluetooth_info")
        //spinner에서 사용할 아이템 목록
        var purpose_data = listOf("사용목적을 선택해 주세요.", "공부", "회의", "스터디")
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, purpose_data)
        binding.purposeSpinner.adapter = adapter

        binding.purposeSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.purposeTxt.text = purpose_data.get(position)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
        //여기까지가 spinner관련 코드

        //문의하기 버튼 listener
        binding.requestBtn.setOnClickListener {
            startActivity(Intent(this@MainOption, Send_email::class.java))
        }

        //자리비움 버튼 클릭시에 Alert Dialog 띄워줌
        binding.emptyBtn.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("자리 비움")
                .setMessage("자리를 비울 수 있는 시간은 10분입니다.")
                .setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, id ->
                    //이부분에 서버에 10분 딜레이주는 코드 추가해야함
                    //

                })
                .setNegativeButton("취소", DialogInterface.OnClickListener { dialogInterface, id ->

                })
            builder.show()
        }
        //

        //액션바 상에 뒤로가기 버튼 관련된 코드, 밑에 있는 onOptionsItemSelected함수를 콜백함
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    //parameter = The menu item that was selected. This value cannot be null.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
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