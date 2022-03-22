package com.akj.nulloverflow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.akj.nulloverflow.databinding.ActivityMainOptionBinding

class MainOption : AppCompatActivity() {

    private val binding by lazy { ActivityMainOptionBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
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

        //액션바 상에 뒤로가기 버튼 관련된 코드, 밑에 있는 onOptionsItemSelected함수를 콜백함
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    //parameter = The menu item that was selected. This value cannot be null.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
}