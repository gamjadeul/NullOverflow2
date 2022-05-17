package com.akj.nulloverflow

import com.google.gson.JsonElement
import okhttp3.ResponseBody
import org.json.JSONObject
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


    /*
    @GET("/bti/bluetooth_get")
    fun getSusBleInfo(@Query("mac") infoEle: String) : Call<JsonElement>
     */

    /*
    https://gp34e91r3a.execute-api.ap-northeast-2.amazonaws.com/bti/bluetooth_update -> PUT에 해당하는 url
    + /bti/bluetooth_update/?mac="searchMac"&purpose="pudatePur"&stat="updateStat" body를 넘겨주는 방식이 아닌 다음과 같이 Field로 바로 넘겨주는 방식
    (form-urlencoded방식 - @FormUrlEncoded라는 어노테이션이 반드시 필요함)
    form-urlencoded방식을 보내면 안감 -> 로그 찍어보니까 뒤에 쿼리문이 안붙음, GET Method처럼 Query로 붙이니까 됨
     */


    @PUT("/bti/bluetooth_update")
    fun updateInfo(@Query("mac") searchMac: String,
                   @Query("purpose") updatePur: String,
                   @Query("stat") updateStat: Boolean) : Call<ResponseBody>

}