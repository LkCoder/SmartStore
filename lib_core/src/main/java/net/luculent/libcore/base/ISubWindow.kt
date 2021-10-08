package net.luculent.libcore.base

/**
 *
 * @Description:     页面弹框或者提示
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/8 14:33
 */
interface ISubWindow {
    fun showToast(resId: Int) {}
    fun showToast(tip: CharSequence) {}
    fun showDialog() {}
}