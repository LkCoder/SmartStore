package net.luculent.smartstore.api

import com.blankj.utilcode.util.LogUtils
import net.luculent.libapi.http.DefaultConfiguration
import net.luculent.libapi.http.HttpLogger
import net.luculent.libapi.mock.MockConfiguration
import net.luculent.smartstore.BuildConfig

/**
 *
 * @Description:     api配置
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/12 16:10
 */
class ApiConfiguration : DefaultConfiguration() {

    override fun baseUrl(): String {
        return "http://47.97.105.118:8041/Liems/webservice"
    }

    private var httpLogger: HttpLogger = object : HttpLogger {
        override fun log(info: String) {
            LogUtils.i(info)
        }
    }

    override fun logger(): HttpLogger? {
        return httpLogger
    }

    fun getMockConfiguration(): MockConfiguration {
        return MockConfiguration(
            BuildConfig.DEBUG, arrayListOf(
                "api.json"
            )
        )
    }
}