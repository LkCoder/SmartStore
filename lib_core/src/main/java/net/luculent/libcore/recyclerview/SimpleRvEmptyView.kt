package net.luculent.libcore.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.store_simple_empty_layout.view.*
import net.luculent.libcore.R

/**
 * @Description: 简单的空布局
 * @Author: yanlei.xia
 * @CreateDate: 2021/10/15 9:48
 */
class SimpleRvEmptyView @JvmOverloads constructor(
    context: Context,
    tipTxt: CharSequence? = null,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        inflate(context, R.layout.store_simple_empty_layout, this)
        empty_tv.text = tipTxt
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.i("SimpleRvEmptyView", "onLayout: [$measuredHeight, $measuredWidth]")
    }
}