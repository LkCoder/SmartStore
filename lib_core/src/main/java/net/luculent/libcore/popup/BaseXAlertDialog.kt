package net.luculent.libcore.popup

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.AdaptScreenUtils
import com.blankj.utilcode.util.ScreenUtils
import net.luculent.libcore.widget.WindowViewContainer

/**
 *
 * @Description:     alertdialog
 * @Author:         yanlei.xia
 * @CreateDate:     2021/11/19 13:45
 */
abstract class BaseXAlertDialog(context: Context) : AlertDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val windowViewContainer = WindowViewContainer(context)
        windowViewContainer.setContent(getLayoutId(), Gravity.CENTER)
        setContentView(windowViewContainer)
    }

    override fun onStart() {
        super.onStart()
        adaptPopSize()
    }

    private fun adaptPopSize() {
        val popSize = getDesignSize()
        val width = ScreenUtils.getAppScreenWidth() * popSize.popWidth / popSize.windowWidth
        AdaptScreenUtils.adaptWidth(context.resources, popSize.windowWidth)
        window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    override fun show() {
        if (isShowing) {
            return
        }
        if (context is Activity && (context as Activity).isFinishing) {
            return
        }
        super.show()
    }

    abstract fun getLayoutId(): Int
    abstract fun getDesignSize(): DesignPopSize
}