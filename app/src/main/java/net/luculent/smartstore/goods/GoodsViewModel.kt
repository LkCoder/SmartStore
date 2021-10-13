package net.luculent.smartstore.goods

import androidx.lifecycle.MutableLiveData
import net.luculent.libcore.mvvm.BaseViewModel
import net.luculent.smartstore.api.ApiService
import net.luculent.smartstore.api.request.GoodsListReq
import net.luculent.smartstore.api.response.PickDetailResp

/**
 *
 * @Description:     物资逻辑
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/13 16:25
 */
class GoodsViewModel : BaseViewModel() {

    val pickDetailResp by lazy { MutableLiveData<PickDetailResp>() }

    fun getGoodsList(pickNo: String) {
        launch({
            ApiService.get().goodsList(GoodsListReq(pickNo))
        }, {
            pickDetailResp.postValue(it)
        })
    }
}