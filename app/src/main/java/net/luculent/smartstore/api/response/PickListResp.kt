package net.luculent.smartstore.api.response

/**
 *
 * @Description:     领料单列表
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/13 16:16
 */
data class PickListResp(
    val rows: List<PickSheet>?
) : BaseResp()

data class PickSheet(
    val date: String?, // 领料日期
    val deptNam: String?, // 部门
    val orgNam: String?, // 公司名称
    val photo: String?, // 领料人照片
    val pickId: String?, // 领料单编号
    val pickNo: String?, // 领料单主键
    val statusNam: String?, // 状态名称
    val statusNo: String?, // 状态值
    val userId: String?, // 领料人Id
    val userNam: String? // 领料人姓名
)
