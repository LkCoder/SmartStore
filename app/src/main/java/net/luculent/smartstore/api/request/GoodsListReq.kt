package net.luculent.smartstore.api.request

/**
 *
 * @Description:     java类作用描述
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/13 16:24
 */
data class GoodsListReq(
    val pickNo: String,
    val orgno: String? = ""
)
