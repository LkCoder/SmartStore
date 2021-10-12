package net.luculent.smartstore.api

import okhttp3.Interceptor
import okhttp3.Response

/**
 *
 * @Description:     添加统一参数
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/12 18:18
 */
class UrlInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (request.method() == "GET") {

        } else if (request.method() == "POST") {

        }
        TODO("Not yet implemented")
    }
}