package net.luculent.libcore.popup

import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ScreenUtils
import kotlinx.android.synthetic.main.lib_confirm_dialog.*
import net.luculent.libcore.R

/**
 *
 * @Description:     确认对话框
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/14 16:09
 */
class ConfirmDialog(private val configuration: DialogConfiguration? = null) : BaseXDialog() {

    override fun getLayoutId(): Int {
        return R.layout.lib_confirm_dialog
    }

    override fun onStart() {
        super.onStart()
        val width = ScreenUtils.getAppScreenWidth() * 375 / 540
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun initView(view: View) {
        super.initView(view)
        confirm_dialog_cancel.setOnClickListener {
            callBack?.onCancel()
            dismiss()
        }
        confirm_dialog_ok.setOnClickListener {
            callBack?.onConfirm()
            dismiss()
        }
        applyConfiguration(configuration)
    }

    fun applyConfiguration(configuration: DialogConfiguration?) {
        configuration?.apply {
            confirm_dialog_title.setTextColor(titleColor)
            titleSize?.let {
                confirm_dialog_title.textSize = it
            }
            confirm_dialog_title.text = title
            if (title.isNullOrEmpty()) {
                confirm_dialog_title.visibility = View.GONE
            }

            confirm_dialog_content_tv.setTextColor(contentColor)
            contentSize?.let {
                confirm_dialog_content_tv.textSize = it
            }
            confirm_dialog_content_tv.text = content
            if (content.isNullOrEmpty()) {
                confirm_dialog_content_tv.visibility = View.GONE
            }
        }
    }
}