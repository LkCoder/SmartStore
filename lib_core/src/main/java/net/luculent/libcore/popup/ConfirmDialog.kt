package net.luculent.libcore.popup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.lib_confirm_dialog.*
import net.luculent.libcore.R

/**
 *
 * @Description:     确认对话框
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/14 16:09
 */
class ConfirmDialog(private val configuration: DialogConfiguration? = null) : DialogFragment() {

    var callBack: DialogCallBack? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.lib_confirm_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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