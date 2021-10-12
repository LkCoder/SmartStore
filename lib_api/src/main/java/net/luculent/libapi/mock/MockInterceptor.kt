package net.luculent.libapi.mock

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response

/**
 *
 * @Description:     mock数据拦截器
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/12 15:30
 */
class MockInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url().encodedPath()
        if (!MockFactory.acceptMock(path)) {
            return chain.proceed(request)
        }
        val body = MockFactory.request(path)
        return if (body.isNullOrEmpty()) {//没有注册对应的mock服务
            chain.proceed(request)
        } else {
            Response.Builder() //
                .code(200)
                .message("OK")
                .protocol(Protocol.HTTP_1_1)
                .request(Request.Builder().url("http://localhost/").build())
                .body(MockResponseBody(body))
                .build()
        }
    }
}