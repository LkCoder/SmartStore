package net.luculent.smartstore.api

import net.luculent.libapi.http.HttpFactory
import net.luculent.smartstore.api.request.GoodsListReq
import net.luculent.smartstore.api.request.LoginReq
import net.luculent.smartstore.api.request.PickListReq
import net.luculent.smartstore.api.response.PickDetailResp
import net.luculent.smartstore.api.response.PickListResp
import net.luculent.smartstore.api.response.UserInfo
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
}