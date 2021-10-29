package net.luculent.libusb.face.widget

import android.content.Context
import android.graphics.Outline
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import com.serenegiant.widget.UVCCameraTextureView

/**
 *
 * @Description:     预览圆形视图
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/29 18:47
 */
class UVCFaceCameraView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : UVCCameraTextureView(context, attrs, defStyleAttr) {

    private var radius = 0f

    init {
        if (isLOLLIPOPAbove()) {
            outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    val rect = Rect(0, 0, view.measuredWidth, view.measuredHeight)
                    outline.setRoundRect(rect, radius.toFloat())
                }
            }
            clipToOutline = true
        }
    }

    fun setRadius(radius: Float) {
        this.radius = radius
        if (isLOLLIPOPAbove()) {
            invalidateOutline()
        }
    }

    private fun isLOLLIPOPAbove(): Boolean {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP
    }
}