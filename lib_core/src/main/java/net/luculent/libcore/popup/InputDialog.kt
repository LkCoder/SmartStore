package net.luculent.libcore.popup

import android.view.View
import androidx.core.widget.doAfterTextChanged
import kotlinx.android.synthetic.main.lib_input_dialog.*
import net.luculent.libcore.R

/**
 * Created by xiayanlei on 2021/10/14
 */
class InputDialog : BaseXDialog() {

    override fun getLayoutId(): Int {
        return R.layout.lib_input_dialog
    }

    override fun initView(view: View) {
        super.initView(view)
        input_dialog_et.doAfterTextChanged {
            if (it?.toString().isNullOrEmpty()) {
                input_dialog_et_clear.visibility = View.GONE
            } else {
                input_dialog_et_clear.visibility = View.VISIBLE
            }
        }
        input_dialog_et_clear.setOnClickListener {
            input_dialog_et.setText("")
        }
        input_dialog_close_iv.setOnClickListener {
            callBack?.onCancel()
            dismiss()
        }
        input_dialog_confirm_btn.setOnClickListener {
            callBack?.onConfirm()
            dismiss()
        }
    }
}