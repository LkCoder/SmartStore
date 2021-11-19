package net.luculent.face

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.luculent.face.api.FaceApi
import net.luculent.face.api.FaceException
import net.luculent.face.api.request.FaceSearchReq
import net.luculent.face.api.response.AccessToken
import net.luculent.face.api.response.FaceUser
import net.luculent.libapi.http.DefaultConfiguration
import net.luculent.libapi.http.HttpFactory
import net.luculent.libapi.http.HttpLogger
import okhttp3.OkHttpClient

/**
 *
 * @Author:         yanlei.xia
 * @CreateDate:     2021/11/18 17:29
 */
class FaceService private constructor() {

    private var accessToken: String = ""

    fun auth(callBack: FaceCallBack<AccessToken>? = null) {
        request({
            val apiConfig = FaceManager.getFaceConfig().apiConfig
            faceApi().auth(apiConfig.apiKey, apiConfig.secretKey)
        }, {
            accessToken = it.access_token ?: ""
            if (accessToken.isEmpty()) {
                callBack?.onError(FaceException(it.error, it.error_description))
            } else {
                callBack?.onSuccess(it)
            }
        })
    }

    fun verify(imageInBase64: String, callBack: FaceCallBack<FaceUser>) {
        request({
            faceApi().search(
                FaceSearchReq(
                    imageInBase64,
                    "BASE64",
                    FaceManager.getFaceConfig().apiConfig.getGroupIds()
                )
            )
        }, {
            val userList = it.result?.user_list
            if (userList.isNullOrEmpty()) {
                callBack.onError(FaceException(it.error_code?.toString(), it.error_msg))
            } else {
                callBack.onSuccess(userList[0])
            }
        })
    }

    private fun faceApi(): FaceApi {
        return HttpFactory.getService(FaceApi::class.java, object : DefaultConfiguration() {
            override fun httpClient(): OkHttpClient {
                return super.httpClient().newBuilder()
                    .addInterceptor { chain ->
                        val request = chain.request()
                        val path = request.url().encodedPath()
                        val requestBuilder = request.newBuilder()
                        val urlBuilder = request.url().newBuilder()
                        if (!FaceApi.TOKEN_WHITElIST.contains(path)) {
                            urlBuilder.addQueryParameter(
                                "access_token", accessToken
                            )
                        }
                        requestBuilder.url(urlBuilder.build())
                        chain.proceed(requestBuilder.build())
                    }
                    .build()
            }

            override fun logger(): HttpLogger {
                return object : HttpLogger {
                    override fun log(info: String) {
                        FaceLogger.i(info)
                    }
                }
            }
        })
    }

    companion object {

        private val faceService by lazy { FaceService() }

        fun get(): FaceService {
            return faceService
        }

        private fun <T> request(
            requestBlock: suspend () -> T,
            success: (data: T) -> Unit,
            error: (exception: FaceException) -> Unit = {}
        ) {
            MainScope().launch {
                try {
                    val data = withContext(Dispatchers.IO) {
                        requestBlock()
                    }
                    if (data == null) {
                        error.invoke(FaceException("-1", "request result null exception"))
                    } else {
                        success.invoke(data)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    error.invoke(FaceException("-2", e.message ?: "server error"))
                }
            }
        }
    }
}