package net.luculent.smartstore.service

import net.luculent.libcore.storage.Storage
import net.luculent.smartstore.api.response.UserInfo

/**
 *
 * @Description:     用户管理类
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/14 10:32
 */
object UserService {

    fun getUser(): UserInfo? {
        return Storage.getInstance().get<UserInfo>(Constants.USER)
    }
}