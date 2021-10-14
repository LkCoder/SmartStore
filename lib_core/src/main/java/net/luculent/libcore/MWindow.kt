package net.luculent.libcore

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.blankj.utilcode.util.ToastUtils
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
    ToastUtils.showShort(charSequence)
}

@JvmOverloads
fun FragmentActivity.showConfirmDialog(
    configuration: DialogConfiguration,
    callBack: DialogCallBack? = null
) {
    val dialog = ConfirmDialog(configuration)
    dialog.callBack = callBack
    dialog.show(supportFragmentManager, ConfirmDialog::class.simpleName)
}