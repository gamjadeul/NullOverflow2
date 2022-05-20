package com.akj.nulloverflow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.akj.nulloverflow.databinding.ActivityMainBinding
import com.akj.nulloverflow.databinding.ActivityRoomCheckBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class RoomCheck : AppCompatActivity() {
    val binding by lazy { ActivityRoomCheckBinding.inflate(layoutInflater) }

    private lateinit var adapter: RoomCheckAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.roomCheckToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.roomCheckToolBar.title = "좌석현황"

        val roomInfo = RetrofitClient.getClient("https://gp34e91r3a.execute-api.ap-northeast-2.amazonaws.com")?.create(IRetrofit::class.java)
        roomInfo?.getBleInfo("all")?.enqueue(object: Callback<BleTableData> {
            override fun onResponse(call: Call<BleTableData>, response: Response<BleTableData>) {
                adapter = RoomCheckAdapter()
                adapter.roomList = response.body() as BleTableData
                binding.roomCheckRcy.adapter = adapter
                binding.roomCheckRcy.layoutManager = LinearLayoutManager(this@RoomCheck)
            }

            override fun onFailure(call: Call<BleTableData>, t: Throwable) {
                Log.i("RoomCheckTest", "onFailure called / t: $t")
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}