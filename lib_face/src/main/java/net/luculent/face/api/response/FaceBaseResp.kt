package net.luculent.face.api.response

import java.io.Serializable

/**
 *
 * @Description:     请求基类
 * @Author:         yanlei.xia
 * @CreateDate:     2021/11/18 16:40
 */
open class FaceBaseResp : Serializable {
    var error: String? = ""
    var error_description: String? = ""
}