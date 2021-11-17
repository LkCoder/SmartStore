package net.luculent.smartstore.pick

import androidx.lifecycle.MutableLiveData
import net.luculent.libcore.mvvm.BaseViewModel
import net.luculent.libcore.storage.mkv.Storage
import net.luculent.smartstore.api.ApiService
import net.luculent.smartstore.api.response.PickListResp
import net.luculent.smartstore.service.Constants
import net.luculent.smartstore.service.UserService

/**
 *
 * @Description:     领料单逻辑类
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/13 16:19
 */
class PickViewModel : BaseViewModel() {

    val pickListLiveData by lazy { MutableLiveData<PickListResp?>() }

    /**
     * 获取用户的领料单
     */
    fun getPickList() {
        launch({
            val pickListResp = ApiService.get().pickList(
                UserService.getUser()?.userId ?: ""
            )
            Storage.getInstance().put(Constants.PICK_LIST, pickListResp)
            pickListResp
        }, {
            pickListLiveData.postValue(it)
        }, { pickListLiveData.postValue(null) })
    }

    /**
     * 从缓存中获取领料单，并擦除缓存
     */
    fun peekPickList() {
        launch({
            Storage.getInstance().get<PickListResp>(Constants.PICK_LIST)
        }, {
            Storage.getInstance().remove(Constants.PICK_LIST)
            pickListLiveData.postValue(it)
        })
    }
}