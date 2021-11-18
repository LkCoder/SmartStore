package net.luculent.libapi.mock

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import net.luculent.libapi.mock.utils.AssetsUtil

/**
 *
 * @Description:     测试数据
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/12 13:27
 */
@SuppressLint("StaticFieldLeak")
object MockFactory {

    @Volatile
    private var isMockApiLoaded = false
    private val mockApi = mutableMapOf<String, String?>()
    private var mContext: Context? = null
    private var configuration: MockConfiguration? = null

    /**
     * 注册mock的api-json配置文件，key：请求path，value：返回的结构体，like this
     *   {
     *       "login": {//service path and response
     *           "username": "mockUser"
     *       }
     *   }
     */
    fun init(context: Context, configuration: MockConfiguration?) {
        this.mContext = context.applicationContext
        this.configuration = configuration
    }

    /**
     * 判断是否使用mock配置
     */
    fun acceptMock(path: String): Boolean {
        if (configuration?.mockEnabled == true) {
            val includePaths = configuration?.mockPaths
            if (includePaths.isNullOrEmpty()) {
                return true
            }
            return includePaths.contains(path)
        }
        return false
    }

    /**
     * 请求mock数据
     */
    fun request(path: String): String? {
        if (!isMockApiLoaded) {
            mContext?.apply {
                val gson = Gson()
                configuration?.assetPaths?.map {
                    val json = AssetsUtil.readAsString(this, it)
                    val type = object : TypeToken<Map<String, JsonObject?>>() {}.type
                    val apiMap = gson.fromJson<Map<String, JsonObject?>>(json, type)
                    apiMap.map { entry ->
                        mockApi.put(entry.key, entry.value.toString())
                    }
                }
                isMockApiLoaded = true
            }
        }
        return mockApi[path]
    }
}