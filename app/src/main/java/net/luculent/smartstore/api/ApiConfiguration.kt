package net.luculent.smartstore.api

import net.luculent.libapi.http.DefaultConfiguration
import net.luculent.libapi.mock.MockConfiguration
import okhttp3.OkHttpClient

/**
 *
 * @Description:     api配置
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/12 16:10
 */
class ApiConfiguration : DefaultConfiguration() {

    override fun baseUrl(): String {
        return "http://47.97.105.118:8041/Liems/webservice/"
    }

    override fun httpClient(): OkHttpClient {
        return super.httpClient().newBuilder()
            .addInterceptor(UrlInterceptor())
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