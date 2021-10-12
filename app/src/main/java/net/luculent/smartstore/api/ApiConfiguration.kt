package net.luculent.smartstore.api

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

    private var httpLogger: HttpLogger = object : HttpLogger {
        override fun log(info: String) {

        }
    }

    override fun logger(): HttpLogger? {
        return httpLogger
    }

    fun getMockConfiguration(): MockConfiguration {
        return MockConfiguration(BuildConfig.DEBUG, arrayListOf())
    }
}