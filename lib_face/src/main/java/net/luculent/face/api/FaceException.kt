package net.luculent.face.api

/**
 *
 * @Description:     人脸识别异常
 * @Author:         yanlei.xia
 * @CreateDate:     2021/11/18 16:35
 */
data class FaceException(
    val errorCode: String?,
    val errorMsg: String?
)