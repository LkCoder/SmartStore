package net.luculent.libapi.mock

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.BufferedSource
import okio.Okio
import java.io.ByteArrayInputStream
import java.io.InputStream

/**
 * @Description: mock数据拦截器
 * @Author: yanlei.xia
 * @CreateDate: 2021/10/12 13:31
 */
class MockResponseBody(private val response: String) : ResponseBody() {
    override fun contentType(): MediaType? {
        return DEFAULT_MEDIA_TYPE
    }

    override fun contentLength(): Long {
        return response.length.toLong()
    }

    override fun source(): BufferedSource {
        return Okio.buffer(Okio.source(inputStream()))
    }

    private fun inputStream(): InputStream {
        return ByteArrayInputStream(response.toByteArray())
    }

    companion object {
        private val DEFAULT_MEDIA_TYPE = MediaType.parse("application/json;charset=UTF-8")
    }
}