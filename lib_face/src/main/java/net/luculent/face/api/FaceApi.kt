package net.luculent.face.api

import net.luculent.face.api.FaceApi.Companion.FACE_SERVER
import net.luculent.face.api.request.FaceSearchReq
import net.luculent.face.api.response.AccessToken
import net.luculent.face.api.response.FaceVerifyRes
import net.luculent.libapi.http.annotation.BaseUrl
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

/**
 *
 * @Description:     人脸接口
 * @Author:         yanlei.xia
 * @CreateDate:     2021/11/18 14:40
 */
@BaseUrl(FACE_SERVER)
interface FaceApi {

    companion object {
        const val FACE_SERVER = "https://aip.baidubce.com/"
        val TOKEN_WHITElIST = arrayListOf("/oauth/2.0/token")//不需要添加token参数的请求，必须以“/”开头
    }

    @POST("oauth/2.0/token")
    suspend fun auth(
        @Query("client_id") client_id: String,
        @Query("client_secret") client_secret: String,
        @Query("grant_type") grant_type: String = "client_credentials"
    ): AccessToken

    @POST("rest/2.0/face/v3/search")
    suspend fun search(@Body faceSearch: FaceSearchReq): FaceVerifyRes
}