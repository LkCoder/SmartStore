package net.luculent.smartstore.api

import okhttp3.FormBody
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
        val requestBuilder = request.newBuilder()
        if (request.method() == "GET") {

        } else if (request.method() == "POST") {
            val body = request.body()
            if (body is FormBody) {
                val newFormBodyBuilder = FormBody.Builder()
                for (i in 0 until body.size()) {
                    newFormBodyBuilder.add(
                        body.name(i),
                        SafeUtils.Base64StrAesEncrypt(body.value(i))
                    )
                }
                requestBuilder.post(newFormBodyBuilder.build())
            }
        }
        return chain.proceed(requestBuilder.build())
    }
}