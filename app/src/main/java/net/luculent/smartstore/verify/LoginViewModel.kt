package net.luculent.smartstore.verify

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.EncryptUtils
import net.luculent.face.FaceCallBack
import net.luculent.face.FaceManager
import net.luculent.face.api.FaceException
import net.luculent.face.api.response.FaceUser
import net.luculent.libcore.mvvm.BaseViewModel
import net.luculent.libcore.storage.mkv.Storage
import net.luculent.smartstore.api.ApiService
import net.luculent.smartstore.api.response.UserInfo
import net.luculent.smartstore.service.Constants

/**
 *
 * @Description:     用户认证逻辑类
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/12 17:21
 */
class LoginViewModel : BaseViewModel(), FaceCallBack<FaceUser> {

    val loginLiveData by lazy { MutableLiveData<UserInfo>() }
    val faceUserData by lazy { MutableLiveData<FaceUser?>() }

    fun login(userId: String, password: String) {
        launch({
            val realUser = userId.toUpperCase()
            val realPass = EncryptUtils.encryptMD5ToString(realUser + password).toLowerCase()
            ApiService.get().login(
                realUser, realPass
            )
        }, {
            loginLiveData.postValue(it)
        })
    }

    fun faceVerify(context: Context) {
        FaceManager.registerFaceVerifyCallBack(this)
        FaceManager.startFaceVerify(context)
    }

    fun saveUser(user: UserInfo) {
        Storage.getInstance().put(Constants.USER, user)
    }

    override fun onSuccess(result: FaceUser) {
        faceUserData.postValue(result)
    }

    override fun onError(exception: FaceException) {
        faceUserData.postValue(null)
    }

    override fun onCleared() {
        super.onCleared()
        FaceManager.unregisterFaceVerifyCallBack(this)
    }
}