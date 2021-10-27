package net.luculent.smartstore.api

import com.blankj.utilcode.util.LogUtils
import net.luculent.libapi.http.DefaultConfiguration
import net.luculent.libapi.http.HttpLogger
import net.luculent.libapi.mock.MockConfiguration
import net.luculent.libcore.utils.ServerManager
import okhttp3.OkHttpClient

/**
 *
 * @Description:     api配置
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/12 16:10
 */
class ApiConfiguration : DefaultConfiguration() {

    override fun baseUrl(): String {
        return ServerManager.getServer()
    }

    override fun logger(): HttpLogger {
        return object : HttpLogger {
            override fun log(info: String) {
                LogUtils.iTag("OK-Http", info)
            }
        }
    }

    override fun httpClient(): OkHttpClient {
        return super.httpClient().newBuilder()
            .build()
    }

    fun getMockConfiguration(): MockConfiguration {
        return MockConfiguration(
            false, arrayListOf(
                "api.json"
            )
        )
    }
}