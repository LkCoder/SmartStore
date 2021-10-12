package net.luculent.smartstore.api

import net.luculent.smartstore.api.request.LoginReq
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

    @POST("loginJST")
    suspend fun login(@Body req: LoginReq): UserInfo

    @POST("getJSTPickList")
    suspend fun pickList()
}