package com.akj.nulloverflow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.akj.nulloverflow.databinding.ActivityMainBinding
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserState
import com.amazonaws.mobile.client.UserStateDetails
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private var testMap = mutableMapOf<String, Boolean>()
    private var purposeMap = mutableMapOf<String, String>()
    private lateinit var re_adapter: CustomAdapter
    lateinit var info: MutableList<Info>
    lateinit var userAttr: MutableMap<String, String>
    private var timeRenew = mutableMapOf<String, Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //레트로핏 이용하여 모든 값 읽어오기
        val test = RetrofitClient.getClient("https://gp34e91r3a.execute-api.ap-northeast-2.amazonaws.com")?.create(IRetrofit::class.java)

        test?.getBleInfo("all")?.enqueue(object: retrofit2.Callback<BleTableData> {
            override fun onResponse(call: Call<BleTableData>, response: Response<BleTableData>) {
                val testList = response.body() as BleTableData
                extractInfo(testList)
            }

            override fun onFailure(call: Call<BleTableData>, t: Throwable) {
            }
        })

        //맨 위 ActionBar에서 메뉴버튼 만들어주는 부분
        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.mainToolbar.title = "사용현황"
        //

        //옵션 선택창 열리고 안에 있는 요소 클릭할 때 리스너 등록
        binding.mainNavigation.setNavigationItemSelectedListener(this)


        //새로고침 버튼 눌렀을 때 Activity 새로고침침
        binding.refreshBtn.setOnClickListener {
           finish()
           overridePendingTransition(0, 0)
           startActivity(intent)
           overridePendingTransition(0, 0)
        }

        AWSMobileClient.getInstance().initialize(applicationContext, object: Callback<UserStateDetails> {
            override fun onResult(result: UserStateDetails?) {
                //로그인이 되어있는 상태라면
                if(result?.userState == UserState.SIGNED_IN) {
                    userAttr = AWSMobileClient.getInstance().userAttributes
                    val navHeaderView = binding.mainNavigation.getHeaderView(0)
                    val navIdText = navHeaderView.findViewById<TextView>(R.id.testId)
                    navIdText.isSelected = true
                    val navNameText = navHeaderView.findViewById<TextView>(R.id.testName)
                    //UI 변경의 경우 Main Thread에서 이루어져야 함
                    runOnUiThread {
                        navNameText.text = userAttr["name"]
                        navIdText.text = userAttr["email"]
                    }
                }
            }

            //로그인이 되어있지 않고 에러가 나는 상황
            override fun onError(e: Exception?) {
                Log.i("mainActivityTest", "AWSMobileClient initialize error / $e")
            }
        })
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
        val info: MutableList<Info> = mutableListOf()
        var floor: Int
        var stat: Boolean
        lateinit var where: String
        lateinit var mac: String
        lateinit var pur: String
        var time: Long?

        for (no in 1..15) {
            when(no) {
                in 1..3 -> {
                    floor = 2
                    if (no % 3 == 1) {
                        where = "계단쪽 테라스"
                        mac = "7C:EC:79:FE:ED:71"
                        stat = macFinder(mac)
                        pur = purFinder(mac)
                        time = exTime(mac)
                    }
                    else if (no % 3 == 2) {
                        where = "중앙 테라스"
                        mac = "4C:24:98:78:1C:7B"
                        stat = macFinder(mac)
                        pur = purFinder(mac)
                        time = exTime(mac)
                    }
                    else {
                        where = "연구실 옆 테라스"
                        mac = ""
                        stat = macFinder(mac)
                        pur = purFinder(mac)
                        time = exTime(mac)
                    }
                }
                in 4..6 -> {
                    floor = 3
                    if (no % 3 == 1) {
                        where = "계단쪽 테라스"
                        mac = ""
                        stat = macFinder(mac)
                        pur = purFinder(mac)
                        time = exTime(mac)
                    }
                    else if (no % 3 == 2) {
                        where = "중앙 테라스"
                        mac = ""
                        stat = macFinder(mac)
                        pur = purFinder(mac)
                        time = exTime(mac)
                    }
                    else {
                        where = "연구실 옆 테라스"
                        mac = ""
                        stat = macFinder(mac)
                        pur = purFinder(mac)
                        time = exTime(mac)
                    }
                }
                in 7..9 -> {
                    floor = 4
                    if (no % 3 == 1) {
                        where = "계단쪽 테라스"
                        mac = ""
                        stat = macFinder(mac)
                        pur = purFinder(mac)
                        time = exTime(mac)
                    }
                    else if (no % 3 == 2) {
                        where = "중앙 테라스"
                        mac = ""
                        stat = macFinder(mac)
                        pur = purFinder(mac)
                        time = exTime(mac)
                    }
                    else {
                        where = "연구실 옆 테라스"
                        mac = ""
                        stat = macFinder(mac)
                        pur = purFinder(mac)
                        time = exTime(mac)
                    }
                }
                in 10..12 -> {
                    floor = 5
                    if (no % 3 == 1) {
                        where = "계단쪽 테라스"
                        mac = ""
                        stat = macFinder(mac)
                        pur = purFinder(mac)
                        time = exTime(mac)
                    }
                    else if (no % 3 == 2) {
                        where = "중앙 테라스"
                        mac = ""
                        stat = macFinder(mac)
                        pur = purFinder(mac)
                        time = exTime(mac)
                    }
                    else {
                        where = "연구실 옆 테라스"
                        mac = ""
                        stat = macFinder(mac)
                        pur = purFinder(mac)
                        time = exTime(mac)
                    }
                }
                else -> {
                    floor = 6
                    if (no % 3 == 1) {
                        where = "계단쪽 테라스"
                        mac = ""
                        stat = macFinder(mac)
                        pur = purFinder(mac)
                        time = exTime(mac)
                    }
                    else if (no % 3 == 2) {
                        where = "중앙 테라스"
                        mac = ""
                        stat = macFinder(mac)
                        pur = purFinder(mac)
                        time = exTime(mac)
                    }
                    else {
                        where = "연구실 옆 테라스"
                        mac = ""
                        stat = macFinder(mac)
                        pur = purFinder(mac)
                        time = exTime(mac)
                    }
                }
            }
            val cur_info = Info(floor, where, mac, stat, pur, time)
            info.add(cur_info)
        }
        return info
    }

    //body에서 정보 추출해서 각자 map에 저장함
    private fun extractInfo(sourceInfo: BleTableData) {
        for(susInfo in sourceInfo.body){
            testMap[susInfo.mac] = susInfo.stat
            timeRenew[susInfo.mac] = susInfo.expireTimeMil.toLong()
            purposeMap[susInfo.mac] = susInfo.purpose
        }
        info = loadData()
        re_adapter = CustomAdapter()
        re_adapter.listData = info
        binding.seatRecycler.adapter = re_adapter
        binding.seatRecycler.layoutManager = LinearLayoutManager(this)
    }

    private fun exTime(macAdd: String): Long {
        return timeRenew[macAdd]?:0
    }

    //자리의 사용여부를 리턴
    private fun macFinder(macAdd: String?): Boolean {
        return !(testMap[macAdd] == null || testMap[macAdd] == false)
    }

    //자리의 사용목적을 리턴
    private fun purFinder(macAdd: String?) : String {
        return purposeMap[macAdd].toString()
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
                AWSMobileClient.getInstance()
                    .initialize(applicationContext, object : Callback<UserStateDetails?> {
                        override fun onResult(result: UserStateDetails?) {
                            AWSMobileClient.getInstance().signOut()
                            val intent = Intent(this@MainActivity, Signin::class.java)
                            startActivity(intent)
                            finish()
                        }

                        override fun onError(e: Exception) {}
                    })
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