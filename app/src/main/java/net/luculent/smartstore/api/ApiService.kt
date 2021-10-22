package net.luculent.smartstore.api

import net.luculent.libapi.http.HttpFactory
import net.luculent.smartstore.api.response.*
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 *
 * @Description:     接口服务
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/12 16:19
 */
interface ApiService {

    companion object {

        const val SERVER = "http://47.97.105.118:8041/Liems/webservice/"

        fun get(): ApiService {
            return HttpFactory.getService(ApiService::class.java)
        }
    }

    @POST("loginJST")
    @FormUrlEncoded
    suspend fun login(
        @Field("userid") userid: String,
        @Field("password") password: String
    ): UserInfo

    @POST("getJSTPickList")
    @FormUrlEncoded
    suspend fun pickList(
        @Field("userid") userid: String,
        @Field("orgno") orgno: String = ""
    ): PickListResp

    @POST("getJSTPickDetail")
    @FormUrlEncoded
    suspend fun goodsList(
        @Field("pickNo") pickNo: String,
        @Field("orgno") orgno: String = ""
    ): PickDetailResp

    @POST("getScanWZDetailJST")
    @FormUrlEncoded
    suspend fun goodsScanResult(
        @Field("userid") userid: String,
        @Field("pkvalue") pkvalue: String,
        @Field("codestr") codestr: String,
        @Field("orgno") orgno: String = ""
    ): GoodsScanResp

    @POST("executeOutStorage")
    @FormUrlEncoded
    suspend fun outStore(
        @Field("userid") userid: String,
        @Field("pkvalue") pkvalue: String,
        @Field("rows") rows: String,
        @Field("orgno") orgno: String = ""
    ): BaseResp
}