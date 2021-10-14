package net.luculent.smartstore.api.request

/**
 *
 * @Description:     出库请求
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/14 11:22
 */
data class OutStoreReq(
    val userid: String,
    val pkvalue: String,//领料单主键
    val rows: List<OutItem>,
    val orgno: String? = null,
)

data class OutItem(
    val childno: String,//
    val number: String,//
    val ckno: String,//仓库主键
    val kwno: String//库位主键
)
