package net.luculent.libusb.scan

import android.app.Activity
import android.view.KeyEvent
import net.luculent.libusb.lifecycle.UsbWindowCallBackWrapper

/**
 *
 * @Description:     扫描代理类
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/19 10:50
 */
class ScannerProxy : SoftKeyBoardListener.OnSoftKeyBoardChangeListener {

    private var keyManager: ScanKeyManager? = null
    private var isInput = false

    fun install(activity: Activity) {
        val originCallback = activity.window.callback
        activity.window.callback = object : UsbWindowCallBackWrapper(originCallback) {
            override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
                if (event?.keyCode != KeyEvent.KEYCODE_BACK && !isInput) {
                    keyManager?.analysisKeyEvent(event)
                    true
                }
                return super.dispatchKeyEvent(event)
            }

            override fun onDetachedFromWindow() {
                unInstall()
                super.onDetachedFromWindow()
            }
        }
        keyManager = ScanKeyManager(activity as ICodeScan)
        SoftKeyBoardListener.setListener(activity, this)
    }

    fun unInstall() {
        keyManager?.clear()
    }

    override fun keyBoardShow(height: Int) {
        isInput = true
    }

    override fun keyBoardHide(height: Int) {
        isInput = false
    }
}