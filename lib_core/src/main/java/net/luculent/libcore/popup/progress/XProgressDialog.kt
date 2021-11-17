package net.luculent.libcore.popup.progress

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.x_progress_dialog.*
import net.luculent.libcore.R

/**
 *
 * @Description:     进度
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/26 17:28
 */
class XProgressDialog(context: Context) : AlertDialog(context) {

    private var content: CharSequence? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.x_progress_dialog)
    }

    override fun onStart() {//因为onCreate第一次的调用时机在show之后，但是onStart每次show都会执行，所以在onStart的地方处理ui
        super.onStart()
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        if (content.isNullOrEmpty()) {
            x_progress_tv.visibility = View.GONE
        } else {
            x_progress_tv.visibility = View.VISIBLE
        }
        x_progress_tv.text = content
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

    fun show(content: CharSequence?) {
        this.content = content
        show()
    }
}