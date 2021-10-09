package net.luculent.libcore.base

/**
 *
 * @Description:    窗口接口
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/8 11:18
 */
interface IMWindow {
    /**
     * called before onCreate
     */
    fun initWindow(configuration: WindowConfiguration) {}
    fun getLayoutId(): Int
    fun initView() {}
    fun initListener() {}
    fun initObserver() {}
    fun initData() {}
}

data class WindowConfiguration(
    var enableImmersionBar: Boolean
) {
    var windowBackgroundColor: Int = android.R.color.transparent
    var clipToPadding = true
    var fitsSystemWindows = true
    var statusBarColor: Int = android.R.color.transparent
    var isLightMode: Boolean = true
}