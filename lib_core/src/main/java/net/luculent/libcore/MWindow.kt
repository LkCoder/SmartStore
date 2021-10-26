package net.luculent.libcore

import android.content.Context
import android.view.Gravity
import androidx.fragment.app.FragmentActivity
import com.blankj.utilcode.util.ToastUtils
import net.luculent.libcore.popup.BaseXDialog
import net.luculent.libcore.popup.ConfirmDialog
import net.luculent.libcore.popup.DialogCallBack
import net.luculent.libcore.popup.DialogConfiguration

/**
 *
 * @Description:     页面辅助类
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/8 14:35
 */
fun Context.toast(charSequence: CharSequence) {
    appToast(charSequence)
}

fun appToast(charSequence: CharSequence) {
    ToastUtils.getDefaultMaker().apply {
        setGravity(Gravity.TOP, 0, 50)
        setTextSize(16)
    }
    ToastUtils.showShort(charSequence)
}

@JvmOverloads
fun FragmentActivity.showConfirmDialog(
    configuration: DialogConfiguration,
    callBack: DialogCallBack? = null
) {
    val dialog = ConfirmDialog(configuration)
    dialog.callBack = callBack
    showXDialog(dialog)
}

fun <T : BaseXDialog> FragmentActivity.showXDialog(dialog: T) {
    dialog.show(supportFragmentManager, dialog::class.simpleName)
}