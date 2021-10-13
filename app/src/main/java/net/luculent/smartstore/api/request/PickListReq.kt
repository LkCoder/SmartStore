package net.luculent.smartstore.api.request

/**
 *
 * @Description:     领料单请求
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/13 16:14
 */
data class PickListReq(
    val userid: String,
    val orgno: String? = ""
)