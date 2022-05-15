package com.akj.nulloverflow

import com.google.gson.JsonElement
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

//https://gp34e91r3a.execute-api.ap-northeast-2.amazonaws.com/bti/bluetooth_get
interface IRetrofit {

    /*
        https://gp34e91r3a.execute-api.ap-northeast-2.amazonaws.com
        + /bti/bluetooth_get/?mac="" (""안쪽 부분이 infoEle인 String으로 들어가는 부분)
        return 값은 Json Element
     */
    @GET("/bti/bluetooth_get")
    fun getBleInfo(@Query("mac") infoEle: String) : Call<BleTableData>


    /*
    @GET("/bti/bluetooth_get")
    fun getSusBleInfo(@Query("mac") infoEle: String) : Call<JsonElement>
     */
/*
        https://gp34e91r3a.execute-api.ap-northeast-2.amazonaws.com -> 업데이트하는 url인데 POST로 되어있음
        + /bti/bluetooth_update/?purpose="" (""안쪽 부분이 updateEle인 String으로 들어가는 부분)

    @PUT("/bti/bluetooth_update")
    fun updateInfo(@Query("purpose") updateEle: String)
     */

}