package net.luculent.smartstore.api.response

import java.io.Serializable

/**
 *
 * @Description:     结果基类
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/12 16:59
 */
open class BaseResp : Serializable {
    var result: String? = null
    var message: String? = null

    fun isSuccess(): Boolean {
        return result?.equals("success") ?: false
    }
}
