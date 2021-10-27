package net.luculent.smartstore.goods

import androidx.lifecycle.MutableLiveData
import net.luculent.libcore.mvvm.BaseViewModel
import net.luculent.smartstore.api.ApiService
import net.luculent.smartstore.api.response.BaseResp
import net.luculent.smartstore.api.response.Goods
import net.luculent.smartstore.api.response.PickDetailResp
import net.luculent.smartstore.service.UserService
import org.json.JSONArray
import org.json.JSONObject

/**
 *
 * @Description:     物资逻辑
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/13 16:25
 */
class GoodsViewModel : BaseViewModel() {

    val pickDetailResp by lazy { MutableLiveData<PickDetailResp>() }
    val scanResultData by lazy { MutableLiveData<Goods?>() }
    val outStoreData by lazy { MutableLiveData<BaseResp>() }

    private lateinit var pickNo: String
    private val goodsList = mutableListOf<Goods>()

    fun initPick(pickNo: String) {
        this.pickNo = pickNo
    }

    fun getGoodsList() {
        launch({
            ApiService.get().goodsList(pickNo)
        }, { resp ->
            pickDetailResp.postValue(resp)
            goodsList.apply {
                clear()
                resp.rows?.let {
                    addAll(it)
                }
            }
        })
    }

    fun scanGoodsCode(codeStr: String) {
        launch({
            val resp = ApiService.get().goodsScanResult(
                UserService.getUser()?.userId.toString(),
                pickNo, codeStr
            )
            goodsList.find { it.no == resp.childno }
        }, {
            if (it != null) {
                scanResultData.postValue(it.copy())
            } else {
                scanResultData.postValue(it)
            }
        })
    }

    fun outStore(goodsList: MutableList<Goods>) {
        launch({
            val json = JSONArray().apply {
                for (goods in goodsList) {
                    put(JSONObject().apply {
                        put("childno", goods.no)
                        put("number", goods.recQty)
                        put("ckno", goods.ckno)
                        put("kwno", goods.kwno)
                    })
                }
            }
            ApiService.get().outStore(
                UserService.getUser()?.userId.toString(),
                pickNo,
                json.toString()
            )
        }, {
            outStoreData.postValue(it)
        })
    }
}