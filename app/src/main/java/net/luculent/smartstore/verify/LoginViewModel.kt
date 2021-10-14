package net.luculent.smartstore.verify

import androidx.lifecycle.MutableLiveData
import net.luculent.libcore.mvvm.BaseViewModel
import net.luculent.smartstore.api.response.UserInfo
import net.luculent.smartstore.service.LoginService

/**
 *
 * @Description:     用户认证逻辑类
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/12 17:21
 */
class LoginViewModel : BaseViewModel() {

    val loginLiveData by lazy { MutableLiveData<UserInfo>() }

    fun login(userId: String, password: String) {
        launch({
            LoginService.login(userId, password)
        }, {
            loginLiveData.postValue(it)
        })
    }
}