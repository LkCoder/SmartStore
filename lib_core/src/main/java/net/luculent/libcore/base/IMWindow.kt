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
    fun initWindow() {}
    fun getLayoutId(): Int
    fun initView() {}
    fun initListener() {}
    fun initObserver() {}
    fun initData() {}
}