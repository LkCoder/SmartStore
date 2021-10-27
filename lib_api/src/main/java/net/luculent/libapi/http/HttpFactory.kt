package net.luculent.libapi.http

import net.luculent.libapi.http.annotation.BaseUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.ConcurrentHashMap

/**
 *
 * @Description:     网络请求工厂类
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/11 10:24
 */
object HttpFactory {

    private val serviceCache = ConcurrentHashMap<String, Any?>()

    private var mHttpConfiguration: HttpConfiguration = DefaultConfiguration()

    fun init(configuration: HttpConfiguration) {
        mHttpConfiguration = configuration
    }

    @JvmOverloads
    fun <T> getService(clz: Class<T>, configuration: HttpConfiguration? = null): T {
        val cacheKey = clz.canonicalName!!
        if (serviceCache[cacheKey] != null) {
            return serviceCache[cacheKey] as T
        }
        val wrapConfiguration = generateConfiguration(clz, configuration)
        val retrofit = Retrofit.Builder()
            .baseUrl(wrapConfiguration.baseUrl())
            .client(wrapConfiguration.httpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(clz)
        serviceCache[cacheKey] = service
        return service
    }

    fun clearCache() {
        serviceCache.clear()
    }

    private fun <T> generateConfiguration(
        clz: Class<T>,
        configuration: HttpConfiguration?
    ): HttpConfiguration {
        var baseUrl: String? = configuration?.baseUrl()
        if (baseUrl.isNullOrEmpty()) {
            baseUrl = clz.getAnnotation(BaseUrl::class.java)?.value
        }
        if (baseUrl.isNullOrEmpty()) {
            baseUrl = mHttpConfiguration.baseUrl()
        }
        if (baseUrl.isNullOrEmpty()) {
            throw IllegalArgumentException("${clz.canonicalName} base url is null")
        }
        val client = configuration?.httpClient() ?: mHttpConfiguration.httpClient()
        val logger = configuration?.logger() ?: mHttpConfiguration.logger()
        return WrapConfiguration(baseUrl, client, logger)
    }
}