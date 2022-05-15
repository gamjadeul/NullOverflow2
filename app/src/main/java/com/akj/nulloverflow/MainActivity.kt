package com.akj.nulloverflow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.akj.nulloverflow.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    //private var testList: BleTableData? = null
    private var testMap = mutableMapOf<String, Boolean>()
    lateinit var re_adapter: CustomAdapter
    lateinit var info: MutableList<Info>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //레트로핏 이용하여 모든 값 읽어오기 -> 모든 값 읽어오는거 필요 없고 특정 mac주소 읽어와서 true/false보는게 나을수도
        val test = RetrofitClient.getClient("https://gp34e91r3a.execute-api.ap-northeast-2.amazonaws.com")?.create(IRetrofit::class.java)
        /*
        test?.getSusBleInfo("7C:EC:79:FE:ED:71")?.enqueue(object: Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.i("mainActivityTest", "호출 성공 onResponse / response: ${response.body()}")
                Log.i("mainActivityTest", "호출 성공 onResponse / response: ${response.raw()}")
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.i("mainActivityTest", "호출 성공 onResponse / testList: ${testList}")
            }
        })

         */
        test?.getBleInfo("all")?.enqueue(object: Callback<BleTableData> {
            override fun onResponse(call: Call<BleTableData>, response: Response<BleTableData>) {
                val testList = response.body() as BleTableData
                //여기 밖에서는 아예 second를 사용할 수 없게 됨 -> scope가 해당 class로 한정되어 있음
                //testList는 여기에 선언된게 아님에도 불구하고 원래의 값인 null 값을 갖는듯(여기에 값을 넘겨 주기 전에는 원래 null이었음)
                //여기서 뭔가 처리를 해줘야 하는듯
                //한번 값을 받아오고 나서 testView에 띄워주거나 화면에 띄워주는 용도는 가능한데 값을 가지고 있거나 하는건 안되는듯?
                //여기서 반복문 돌려서 값을 저장하는거는 되는듯
                //그렇게 되면 저장할 값 중 purpose는 딱히 필요 없을거 같음, 사용중인지 아닌지만 판단하면 되니까
                //그렇게 되면 해당 class 처음에 null로 선언해줬던 testList필요 없음, 공간만 낭비함
                //val second = response.body() as BleTableData
                //값이 없어지는게 아니라 비동기 작업이라 화면을 띄워주기 전에는 완료가 안되는듯
                //해당 부분을 거쳐야 무조건 화면에 리사이클러 뷰 띄울 수 있게 해줘야할 듯
                //setNotifySetChanged로 알려주면 되려나
                Log.i("mainActivityTest", "호출 성공 onResponse / response: ${response.body()}")
                Log.i("mainActivityTest", "호출 성공 onResponse / response: ${response.raw()}")
                Log.i("mainActivityTest", "호출 성공 onResponse / testList: ${testList.body}")
                extractInfo(testList)
                //Log.i("mainActivityTest", "호출 성공 onResponse / testList: ${second?.body}")
            }

            override fun onFailure(call: Call<BleTableData>, t: Throwable) {
                Log.i("mainActivityTest", "호출 실패 onFailure / t : $t")
            }
        })

        //리사이클러뷰 어댑터 등록하는 부분
        //Log.i("mainActivityTest", "testMap의 값: $testMap")
        /*
        info = loadData()
        re_adapter = CustomAdapter()
        re_adapter.listData = info
        binding.seatRecycler.adapter = re_adapter
        binding.seatRecycler.layoutManager = LinearLayoutManager(this)
         */
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

        //Log.i("mainActivityTest", "찾아보기${testList?.body?.find { "mac" == "7C:EC:79:FE:ED:71" }}")

       /*
        이 부분이 navigation의 header 텍스트를 결정할 부분 setText의 파라미터로 Auth 혹은 Stor에서 받아온 자료가 넘어가야함
        val nav_header_view = binding.mainNavigation.getHeaderView(0)
        val nav_id_text = nav_header_view.findViewById<TextView>(R.id.testId)
        val nav_name_text = nav_header_view.findViewById<TextView>(R.id.testName)
        nav_name_text.setText()
        nav_id_text.setText()
         */

        /*
        로그인 상태인지 로그아웃 상태인지에 따라서 Navigation 메뉴의 텍스트와 아이콘이 변경되어야 한다.
        val nav_menu_assign = binding.mainNavigation.menu.findItem(R.id.log_out)
        nav_menu_assign.setTitle("로그아웃")
        nav_menu_assign.setIcon(getDrawable(R.drawable.ic_baseline_login_24))
         */

    }

    //리사이클러 뷰에서 사용될 데이터를 만들어주는 함수
    //여기에서 MAC주소의 삽입이 필요한데, 이유는 DB에 MAC주소로 접근하기 때문임
    /*
        현재 존재하는 2개의 BLE기기
        2층계단 -> 7C:EC:79:FE:ED:71
        2층중앙 -> 4C:24:98:78:1C:7B
     */
    //이 loadData가 호출되는 시점에 API로 읽어온 값을 저장하는 testList의 값은 null임
    private fun loadData(): MutableList<Info> {
        //Log.i("mainActivityTest", "loadData 진입, testMap: $testMap")
        val info: MutableList<Info> = mutableListOf()
        var floor: Int
        var stat: Boolean
        lateinit var where: String
        lateinit var mac: String
        for (no in 1..15) {
            when(no) {
                in 1..3 -> {
                    floor = 2
                    if (no % 3 == 1) {
                        where = "계단쪽 테라스"
                        mac = "7C:EC:79:FE:ED:71"
                        stat = macFinder(mac)
                    }
                    else if (no % 3 == 2) {
                        where = "중앙 테라스"
                        mac = "4C:24:98:78:1C:7B"
                        stat = macFinder(mac)
                    }
                    else {
                        where = "연구실 옆 테라스"
                        mac = ""
                        stat = macFinder(mac)
                    }
                }
                in 4..6 -> {
                    floor = 3
                    if (no % 3 == 1) {
                        where = "계단쪽 테라스"
                        mac = ""
                        stat = macFinder(mac)
                    }
                    else if (no % 3 == 2) {
                        where = "중앙 테라스"
                        mac = ""
                        stat = macFinder(mac)
                    }
                    else {
                        where = "연구실 옆 테라스"
                        mac = ""
                        stat = macFinder(mac)
                    }
                }
                in 7..9 -> {
                    floor = 4
                    if (no % 3 == 1) {
                        where = "계단쪽 테라스"
                        mac = ""
                        stat = macFinder(mac)
                    }
                    else if (no % 3 == 2) {
                        where = "중앙 테라스"
                        mac = ""
                        stat = macFinder(mac)
                    }
                    else {
                        where = "연구실 옆 테라스"
                        mac = ""
                        stat = macFinder(mac)
                    }
                }
                in 10..12 -> {
                    floor = 5
                    if (no % 3 == 1) {
                        where = "계단쪽 테라스"
                        mac = ""
                        stat = macFinder(mac)
                    }
                    else if (no % 3 == 2) {
                        where = "중앙 테라스"
                        mac = ""
                        stat = macFinder(mac)
                    }
                    else {
                        where = "연구실 옆 테라스"
                        mac = ""
                        stat = macFinder(mac)
                    }
                }
                else -> {
                    floor = 6
                    if (no % 3 == 1) {
                        where = "계단쪽 테라스"
                        mac = ""
                        stat = macFinder(mac)
                    }
                    else if (no % 3 == 2) {
                        where = "중앙 테라스"
                        mac = ""
                        stat = macFinder(mac)
                    }
                    else {
                        where = "연구실 옆 테라스"
                        mac = ""
                        stat = macFinder(mac)
                    }
                }
            }
            val cur_info = Info(floor, where, mac, stat)
            //Log.i("mainActivityTest", "cur_info: $cur_info")
            info.add(cur_info)
        }
        return info
    }

    private fun extractInfo(sourceInfo: BleTableData) {
        for(susInfo in sourceInfo.body){
            testMap[susInfo.mac] = susInfo.stat
        }
        //Log.i("mainActivityTest", "추출 끝, testMap의 값: $testMap")
        //notifyDataSetChange로 먼저 띄워준 다음에 정보 바꿔주는 방법은 별로 안좋은 거 같음 -> 퍼포먼스적인 측면에서는 좋겠지만 기본적으로 자리확인 어플이 정보 다 받고 확인 후 띄워주는 듯
        info = loadData()
        re_adapter = CustomAdapter()
        re_adapter.listData = info
        binding.seatRecycler.adapter = re_adapter
        binding.seatRecycler.layoutManager = LinearLayoutManager(this)
        //Log.i("mainActivityTest", "추출 후 어댑터 등록 완료")
    }

    private fun macFinder(macAdd: String?): Boolean {
        //Log.i("mainActivityTest", "macFinder진입 testMap: $testMap")
        return !(testMap[macAdd] == null || testMap[macAdd] == false)
    }

    //Action Bar에서 옵션버튼 눌렸을 때 Drawer Layout이 나옴
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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