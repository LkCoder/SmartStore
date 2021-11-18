package net.luculent.face

import net.luculent.face.api.FaceException

/**
 *
 * @Description:     认证回调
 * @Author:         yanlei.xia
 * @CreateDate:     2021/11/18 16:04
 */
interface FaceCallBack<T> {
    fun onSuccess(result: T)
    fun onError(exception: FaceException)
}