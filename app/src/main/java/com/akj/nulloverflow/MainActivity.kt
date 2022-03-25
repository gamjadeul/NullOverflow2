package com.akj.nulloverflow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.akj.nulloverflow.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //리사이클러뷰 어댑터 등록하는 부분
        val info:MutableList<Info> = loadData()
        var re_adapter = CustomAdapter()
        re_adapter.listData = info
        binding.seatRecycler.adapter = re_adapter
        binding.seatRecycler.layoutManager = LinearLayoutManager(this)
        //

        //맨 위 ActionBar에서 메뉴버튼 만들어주는 부분
        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.mainToolbar.title = "사용현황"
        //

        //옵션 선택창 열리고 안에 있는 요소 클릭할 때 리스너 등록
        binding.mainNavigation.setNavigationItemSelectedListener(this)
        //

        /*
        이 부분이 navigation의 header 텍스트를 결정할 부분 setText의 파라미터로 Auth 혹은 Stor에서 받아온 자료가 넘어가야함
        val nav_header_view = binding.mainNavigation.getHeaderView(0)
        val nav_id_text = nav_header_view.findViewById<TextView>(R.id.testId)
        val nav_name_text = nav_header_view.findViewById<TextView>(R.id.testName)
        nav_name_text.setText()
        nav_id_text.setText()
         */

    }

    //리사이클러 뷰에서 사용될 데이터를 만들어주는 함수
    private fun loadData(): MutableList<Info> {
        val info: MutableList<Info> = mutableListOf()
        var floor: Int = 0
        lateinit var where: String
        for (no in 1..15) {
            when(no) {
                in 1..3 -> {
                    floor = 2
                    if (no % 3 == 1) {
                        where = "계단쪽 테라스"
                    }
                    else if (no % 3 == 2) {
                        where = "중앙 테라스"
                    }
                    else {
                        where = "연구실 옆 테라스"
                    }
                }
                in 4..6 -> {
                    floor = 3
                    if (no % 3 == 1) {
                        where = "계단쪽 테라스"
                    }
                    else if (no % 3 == 2) {
                        where = "중앙 테라스"
                    }
                    else {
                        where = "연구실 옆 테라스"
                    }
                }
                in 7..9 -> {
                    floor = 4
                    if (no % 3 == 1) {
                        where = "계단쪽 테라스"
                    }
                    else if (no % 3 == 2) {
                        where = "중앙 테라스"
                    }
                    else {
                        where = "연구실 옆 테라스"
                    }
                }
                in 10..12 -> {
                    floor = 5
                    if (no % 3 == 1) {
                        where = "계단쪽 테라스"
                    }
                    else if (no % 3 == 2) {
                        where = "중앙 테라스"
                    }
                    else {
                        where = "연구실 옆 테라스"
                    }
                }
                else -> {
                    floor = 6
                    if (no % 3 == 1) {
                        where = "계단쪽 테라스"
                    }
                    else if (no % 3 == 2) {
                        where = "중앙 테라스"
                    }
                    else {
                        where = "연구실 옆 테라스"
                    }
                }
            }
            var cur_info = Info(floor, where)
            info.add(cur_info)
        }
        return info
    }

    //Action Bar에서 옵션버튼 눌렸을 때 Drawer Layout이 나옴
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                binding.mainDrawer.openDrawer(GravityCompat.START)
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    //해당 NavigationItemSelected Listener에 로그아웃 및 다른 기능들 구현해야함 -> 로그인 상태일 경우 '로그아웃'이라는 문구가 뜨도록
    //로그아웃 후에도 해당 액티비티에 남아 있어야 함, 사용자 정보와 이메일 등이 지워진 상태로
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.main_option -> {
                startActivity(Intent(this, MainOption::class.java))
                binding.mainDrawer.closeDrawers()
                return true
            }
            R.id.log_out -> {
                //이 위에 파이어베이스 관련해서 로그아웃하는 코드가 들어가야 됨
                binding.mainDrawer.closeDrawers()
            }
        }
        return false
    }

    override fun onBackPressed() {
        if(binding.mainDrawer.isDrawerOpen(GravityCompat.START)) {
            binding.mainDrawer.closeDrawers()
        }
        else {
            super.onBackPressed()
        }
    }
}