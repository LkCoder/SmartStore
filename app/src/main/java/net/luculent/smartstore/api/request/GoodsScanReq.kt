package net.luculent.smartstore.api.request

/**
 *
 * @Description:     物资明细请求
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/14 14:05
 */
data class GoodsScanReq(
    val userid: String,
    val pkvalue: String,
    val codestr: String,
    val orgno: String? = null
)
