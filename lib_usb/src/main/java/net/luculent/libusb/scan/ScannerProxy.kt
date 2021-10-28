package net.luculent.libusb.scan

import android.app.Activity
import android.view.KeyEvent
import net.luculent.libusb.AbsUsbWindowProxy

/**
 *
 * @Description:     扫描代理类
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/19 10:50
 */
class ScannerProxy(activity: Activity) : AbsUsbWindowProxy(activity),
    SoftKeyBoardListener.OnSoftKeyBoardChangeListener {

    private var keyManager: ScanKeyManager? = null
    private var isInput = false

    override fun install() {
        super.install()
        keyManager = ScanKeyManager(activity as ICodeScan)
        SoftKeyBoardListener.setListener(activity, this)
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (event?.keyCode != KeyEvent.KEYCODE_BACK && !isInput) {
            keyManager?.analysisKeyEvent(event)
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onDetachedFromWindow() {
        keyManager?.clear()
        super.onDetachedFromWindow()
    }

    override fun keyBoardShow(height: Int) {
        isInput = true
    }

    override fun keyBoardHide(height: Int) {
        isInput = false
    }
}