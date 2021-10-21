package net.luculent.libcore.storage.mkv

import java.io.Serializable

/**
 *
 * @Description:     存储对象
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/13 13:32
 */
data class InnerStorageBean<T>(
    val value: T?
) : Serializable