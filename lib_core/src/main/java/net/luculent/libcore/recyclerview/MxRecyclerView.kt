package net.luculent.libcore.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.yanzhenjie.recyclerview.OnItemMenuClickListener
import com.yanzhenjie.recyclerview.SwipeMenuCreator
import kotlinx.android.synthetic.main.mx_recyclerview_layout.view.*
import net.luculent.libcore.R

/**
 * @Description: 自定义recyclerview
 * @Author: yanlei.xia
 * @CreateDate: 2021/10/15 10:39
 */
open class MxRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(
    context, attrs, defStyle
), IRecyclerView {

    private val mAdapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            checkIfEmpty()
        }
    }

    init {
        inflate(context, R.layout.mx_recyclerview_layout, this)
    }

    override fun addItemDecoration(decor: ItemDecoration) {
        mx_recyclerview.addItemDecoration(decor)
    }

    override fun setLayoutManager(layout: RecyclerView.LayoutManager?) {
        mx_recyclerview.layoutManager = layout
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        adapter?.registerAdapterDataObserver(mAdapterDataObserver)
        mx_recyclerview.adapter = adapter
    }

    /**
     * Set to create menu listener.
     */
    fun setSwipeMenuCreator(menuCreator: SwipeMenuCreator?) {
        mx_recyclerview.setSwipeMenuCreator(menuCreator)
    }

    /**
     * Set to click menu listener.
     */
    fun setOnItemMenuClickListener(listener: OnItemMenuClickListener?) {
        mx_recyclerview.setOnItemMenuClickListener(listener)
    }

    fun setEmptyView(view: View) {
        mx_empty_container.removeAllViewsInLayout()
        mx_empty_container.addView(view)
    }

    private fun checkIfEmpty() {
        val count = mx_recyclerview.adapter?.itemCount
        if (count == 0) mx_empty_container.visibility =
            View.VISIBLE else mx_empty_container.visibility = View.GONE
    }
}