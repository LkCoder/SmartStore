package net.luculent.libcore.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.blankj.utilcode.util.ColorUtils
import me.jessyan.autosize.AutoSizeCompat
import net.luculent.libcore.base.WindowConfiguration

/**
 * @Description: 页面窗口容器
 * @Author: yanlei.xia
 * @CreateDate: 2021/10/9 9:58
 */
class WindowViewContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        AutoSizeCompat.autoConvertDensityOfGlobal(resources)
        return super.generateLayoutParams(attrs)
    }

    fun initConfiguration(configuration: WindowConfiguration) {
        setBackgroundColor(ColorUtils.getColor(configuration.windowBackgroundColor))
        if (configuration.enableImmersionBar) {
            clipToPadding = configuration.clipToPadding
            fitsSystemWindows = configuration.fitsSystemWindows
        } else {
            clipToPadding = false
            fitsSystemWindows = false
        }
    }

    fun setContent(layoutResID: Int, gravity: Int = Gravity.NO_GRAVITY) {
        removeAllViewsInLayout()
        val view = LayoutInflater.from(context).inflate(layoutResID, this, false)
        addView(view)
        if (gravity != Gravity.NO_GRAVITY) {
            (view.layoutParams as LayoutParams).gravity = gravity
        }
    }
}