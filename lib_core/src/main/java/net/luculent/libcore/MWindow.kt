package net.luculent.libcore

import android.content.Context
import com.blankj.utilcode.util.ToastUtils

/**
 *
 * @Description:     页面辅助类
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/8 14:35
 */
fun Context.toast(charSequence: CharSequence) {
    ToastUtils.showShort(charSequence)
}

fun showDialog() {

}