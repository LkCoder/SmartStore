package net.luculent.libapi.http

import net.luculent.libapi.mock.MockInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

/**
 *
 * @Description:     网络请求配置
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/11 10:31
 */
interface HttpConfiguration {
    fun baseUrl(): String = ""
    fun httpClient(): OkHttpClient
    fun logger(): HttpLogger? = null
}

const val OKHTTP_READ_TIMEOUT = 60L
const val OKHTTP_WRITE_TIMEOUT = 60L
const val OKHTTP_CONNECT_TIMEOUT = 60L

open class DefaultConfiguration : HttpConfiguration {

    private val httpLoggingInterceptor by lazy {
        val httpLogger = logger()
        if (httpLogger == null) {
            HttpLoggingInterceptor()
        } else {
            HttpLoggingInterceptor {
                httpLogger.log(it)
            }
        }
    }

    override fun httpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY))
            .readTimeout(OKHTTP_READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(OKHTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(OKHTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .hostnameVerifier { _, _ -> true }
            .sslSocketFactory(sslSocketFactory, trustManager[0] as X509TrustManager)
            .build()
    }

    private val sslSocketFactory: SSLSocketFactory
        get() = try {
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustManager, SecureRandom())
            sslContext.socketFactory
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    private val trustManager: Array<TrustManager>
        get() {
            val factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            factory.init(null as KeyStore?)
            val managers = factory.trustManagers
            val trustManager = managers[0] as X509TrustManager
            return arrayOf(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                        trustManager.checkClientTrusted(chain, authType)
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                        trustManager.checkServerTrusted(chain, authType)
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return trustManager.acceptedIssuers
                    }
                }
            )
        }
}

class WrapConfiguration(private val baseUrl: String, private val client: OkHttpClient) :
    HttpConfiguration {

    override fun baseUrl(): String {
        return baseUrl
    }

    override fun httpClient(): OkHttpClient {
        return client.newBuilder().addInterceptor(MockInterceptor()).build()
    }
}