package net.luculent.smartstore.verify

import net.luculent.libcore.mvvm.BaseViewModel
import net.luculent.libcore.storage.Storage
import net.luculent.smartstore.api.ApiService
import net.luculent.smartstore.api.request.LoginReq

/**
 *
 * @Description:     用户认证逻辑类
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/12 17:21
 */
class LoginViewModel : BaseViewModel() {

    fun login(userId: String, password: String) {
        launch({
            ApiService.get().login(
                LoginReq(userId, password)
            )
        }, {
            Storage.getInstance().put("user", it)
        })
    }
}