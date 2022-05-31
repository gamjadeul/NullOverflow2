package com.akj.nulloverflow

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

//https://gp34e91r3a.execute-api.ap-northeast-2.amazonaws.com/bti/bluetooth_get
interface IRetrofit {

    /*
        https://gp34e91r3a.execute-api.ap-northeast-2.amazonaws.com
        + /bti/bluetooth_get/?mac="" (""안쪽 부분이 infoEle인 String으로 들어가는 부분)
        return 값은 Json Element
     */
    @GET("/bti/bluetooth_get")
    fun getBleInfo(@Query("mac") infoEle: String) : Call<BleTableData>


    @PUT("/bti/bluetooth_update")
    fun updateInfo(@Query("mac") searchMac: String,
                   @Query("email") userEmail: String?,
                   @Query("expireTimeMil") exTimeMil: Long,
                   @Query("location") fixedLocation: String,
                   @Query("purpose") updatePur: String,
                   @Query("stat") updateStat: Boolean,
                   @Query("timelog") usingTime: String) : Call<ResponseBody>

}