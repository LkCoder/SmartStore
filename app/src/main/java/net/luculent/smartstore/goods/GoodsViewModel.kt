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
    private val goodsCountMap = mutableMapOf<String, Int>()

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
                var count = goodsCountMap[it.no] ?: 0
                count++
                goodsCountMap[it.no] = count
                scanResultData.postValue(it.copy(storecount = count.toString()))
            } else {
                scanResultData.postValue(it)
            }
        })
    }

    fun outStore(goodsList: MutableList<Goods>) {
        launch({
            val json = JSONObject().apply {
                val aar = JSONArray()
                for (goods in goodsList) {
                    aar.put(JSONObject().apply {
                        put("childno", goods.no)
                        put("number", goods.storecount)
                        put("ckno", goods.ckno)
                        put("kwno", goods.kwno)
                    })
                }
                put("rows", aar)
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

    override fun onCleared() {
        super.onCleared()
        goodsCountMap.clear()
    }
}