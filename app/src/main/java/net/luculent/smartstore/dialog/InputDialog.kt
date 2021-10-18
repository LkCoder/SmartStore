package net.luculent.smartstore.dialog

import android.view.View
import androidx.core.widget.doAfterTextChanged
import kotlinx.android.synthetic.main.store_code_input_dialog.*
import net.luculent.libcore.popup.BaseXDialog
import net.luculent.smartstore.R

/**
 * Created by xiayanlei on 2021/10/14
 */
class InputDialog : BaseXDialog() {

    var callBack: CodeInputCallBack? = null

    override fun getLayoutId(): Int {
        return R.layout.store_code_input_dialog
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
            doConfirm()
        }
    }

    private fun doConfirm() {
        val code = input_dialog_et.text.toString()
        if (code.isEmpty()) {
            setError(getString(R.string.store_input_code_tip))
            return
        }
        callBack?.onConfirm(input_dialog_et.text.toString())
        dismiss()
    }

    fun setError(tip: String) {
        input_dialog_error_tv.text = tip
        input_dialog_error_tv.visibility = View.VISIBLE
    }

    interface CodeInputCallBack {
        fun onCancel() {}
        fun onConfirm(code: String) {}
    }
}