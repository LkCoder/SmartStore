package net.luculent.face.api.response

/**
 *
 * @Author:         yanlei.xia
 * @CreateDate:     2021/11/18 14:59
 */
data class AccessToken(
    val access_token: String?, // 要获取的Access Token
    val expires_in: Int?, // Access Token的有效期(秒为单位，有效期30天)
    var error: String?,
    var error_description: String?,
)