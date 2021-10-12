package net.luculent.smartstore.api.response

/**
 *
 * @Description:     用户信息
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/12 16:58
 */
data class UserInfo(
    val userId: String?, // 用户id
    val userNam: String? // 用户名称
): BaseResp()