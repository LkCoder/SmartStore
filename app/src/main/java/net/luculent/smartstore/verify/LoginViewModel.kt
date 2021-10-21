package net.luculent.smartstore.verify

import androidx.lifecycle.MutableLiveData
import net.luculent.libcore.mvvm.BaseViewModel
import net.luculent.libcore.storage.mkv.Storage
import net.luculent.smartstore.api.ApiService
import net.luculent.smartstore.api.request.LoginReq
import net.luculent.smartstore.api.response.UserInfo
import net.luculent.smartstore.service.Constants

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
            ApiService.get().login(
                LoginReq(userId, password)
            )
        }, {
            loginLiveData.postValue(it)
        })
    }

    fun saveUser(user: UserInfo) {
        Storage.getInstance().put(Constants.USER, user)
    }
}