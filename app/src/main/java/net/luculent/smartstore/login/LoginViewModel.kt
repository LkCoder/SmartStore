package net.luculent.smartstore.login

import net.luculent.libapi.http.HttpFactory
import net.luculent.libcore.mvvm.BaseViewModel
import net.luculent.smartstore.api.ApiService
import net.luculent.smartstore.api.request.LoginReq

/**
 *
 * @Description:     登录逻辑类
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/12 17:21
 */
class LoginViewModel : BaseViewModel() {

    fun login(userId: String, password: String) {
        launch({
            HttpFactory.getService(ApiService::class.java).login(
                LoginReq(userId, password)
            )
        }, {

        })
    }
}