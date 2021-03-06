package net.luculent.smartstore.api.response

/**
 *
 * @Description:     java类作用描述
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/13 16:26
 */
data class PickDetailResp(
    val date: String?, // 领料日期
    val userNam: String?, // 领料人姓名
    val deptNam: String?, // 部门
    val photo: String?, // 领料人照片
    val pickId: String?, // 领料单主键
    val rows: List<Goods>?,
    val statusNam: String?, // 状态名称
    val statusNo: String?, // 状态值
    val userId: String? // 领料人Id
) : BaseResp()

data class Goods(
    val id: String?, // 物资编号
    val name: String?, // 物资名称
    val no: String, // 物资主键
    val photo: String?,//物资图片
    val spec: String?, // 规格
    val statusNam: String?, // 状态名称
    val statusNo: String?, // 状态值
    val storecount: String?, // 数量
    val recQty: String?,//领用数量
    val storeloc: String?, // 库位
    val ckno: String?,//
    val kwno: String,//
    val unit: String? // 单位
)