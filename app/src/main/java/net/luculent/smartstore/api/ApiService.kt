package net.luculent.smartstore.api

import net.luculent.libapi.http.HttpFactory
import net.luculent.smartstore.api.request.*
import net.luculent.smartstore.api.response.*
import retrofit2.http.Body
import retrofit2.http.POST

/**
 *
 * @Description:     接口服务
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/12 16:19
 */
interface ApiService {

    companion object {
        fun get(): ApiService {
            return HttpFactory.getService(ApiService::class.java)
        }
    }

    @POST("loginJST")
    suspend fun login(@Body req: LoginReq): UserInfo

    @POST("getJSTPickList")
    suspend fun pickList(@Body req: PickListReq): PickListResp

    @POST("getJSTPickDetail")
    suspend fun goodsList(@Body req: GoodsListReq): PickDetailResp

    @POST("getScanWZDetailJST")
    suspend fun goodsScanResult(@Body req: GoodsScanReq): GoodsScanResp

    @POST("executeOutStorage")
    suspend fun outStore(@Body req: OutStoreReq): BaseResp
}