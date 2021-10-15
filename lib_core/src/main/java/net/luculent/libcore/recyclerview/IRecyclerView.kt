package net.luculent.libcore.recyclerview

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 *
 * @Description:     recyclerview接口
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/15 10:48
 */
interface IRecyclerView {
    fun addItemDecoration(decor: ItemDecoration) {}

    fun setLayoutManager(layout: RecyclerView.LayoutManager?) {}

    fun setAdapter(adapter: RecyclerView.Adapter<*>?) {}
}