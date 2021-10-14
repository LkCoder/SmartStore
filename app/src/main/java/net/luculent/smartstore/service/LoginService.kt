package net.luculent.smartstore.service

import net.luculent.libcore.storage.Storage
import net.luculent.smartstore.api.ApiService
import net.luculent.smartstore.api.request.LoginReq
import net.luculent.smartstore.api.response.UserInfo

/**
 *
 * @Description:     登录服务
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/14 10:35
 */
object LoginService {

    suspend fun login(userId: String, password: String): UserInfo {
        val user = ApiService.get().login(
            LoginReq(userId, password)
        )
        Storage.getInstance().put(Constants.USER, user)
        return user
    }
}