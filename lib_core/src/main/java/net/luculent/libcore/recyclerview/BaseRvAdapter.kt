package net.luculent.libcore.recyclerview

import com.chad.library.adapter.base.BaseQuickAdapter
import com.yanzhenjie.recyclerview.SwipeMenuLayout

/**
 *
 * @Description:     java类作用描述
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/13 16:47
 */
abstract class BaseRvAdapter<T, H : BaseRvHolder>(layoutId: Int) :
    BaseQuickAdapter<T, H>(layoutId) {
    override fun onBindViewHolder(holder: H, position: Int, payloads: MutableList<Any>) {
        if (holder.itemView is SwipeMenuLayout) {
            holder.itemView.smoothCloseMenu(0)
        }
        super.onBindViewHolder(holder, position, payloads)
    }
}