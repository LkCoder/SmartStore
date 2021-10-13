package net.luculent.libcore.recyclerview

import com.chad.library.adapter.base.BaseQuickAdapter

/**
 *
 * @Description:     java类作用描述
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/13 16:47
 */
abstract class BaseRvAdapter<T, H : BaseRvHolder>(layoutId: Int) :
    BaseQuickAdapter<T, H>(layoutId) {
}