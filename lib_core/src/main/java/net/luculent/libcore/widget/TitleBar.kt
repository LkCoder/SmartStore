package net.luculent.libcore.widget

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.blankj.utilcode.util.ConvertUtils
import kotlinx.android.synthetic.main.title_bar_layout.view.*
import net.luculent.libcore.R

/**
 * @Description: 导航栏
 * @Author: yanlei.xia
 * @CreateDate: 2021/10/9 11:15
 */
class TitleBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        inflate(context, R.layout.title_bar_layout, this)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar)

        val barBackground = typedArray.getDrawable(R.styleable.TitleBar_barBackground)
        val backIcon = typedArray.getDrawable(R.styleable.TitleBar_backIcon)
        val backTxt = typedArray.getString(R.styleable.TitleBar_backText)
        val backTxtColor = typedArray.getColor(
            R.styleable.TitleBar_backTextColor,
            context.resources.getColor(android.R.color.white)
        )
        val backTxtSize = typedArray.getDimensionPixelSize(
            R.styleable.TitleBar_backTextSize,
            ConvertUtils.dp2px(20f)
        )
        val titleTxtColor = typedArray.getColor(
            R.styleable.TitleBar_titleTextColor,
            context.resources.getColor(android.R.color.white)
        )
        val titleTxt = typedArray.getString(R.styleable.TitleBar_titleText)
        val titleTxtSize = typedArray.getDimensionPixelSize(
            R.styleable.TitleBar_backTextSize,
            ConvertUtils.dp2px(24f)
        )

        val subContent = typedArray.getResourceId(R.styleable.TitleBar_subContent, -1)
        typedArray.recycle()

        bar_appLayout.background = barBackground
        bar_back_tv.text = backTxt
        bar_back_tv.setTextColor(backTxtColor)
        bar_back_tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, backTxtSize.toFloat())
        bar_back_tv.setCompoundDrawablesWithIntrinsicBounds(backIcon, null, null, null)

        bar_title_tv.text = titleTxt
        bar_title_tv.setTextColor(titleTxtColor)
        bar_title_tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTxtSize.toFloat())

        if (subContent > 0) {
            bar_sub_content.addView(
                LayoutInflater.from(context).inflate(subContent, bar_sub_content, false)
            )
        }
    }

    fun setTitle(title: CharSequence) {
        bar_title_tv.text = title
    }
}