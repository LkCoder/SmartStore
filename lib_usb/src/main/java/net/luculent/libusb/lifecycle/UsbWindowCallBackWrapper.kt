package net.luculent.libusb.lifecycle

import android.os.Build
import android.view.*
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi

/**
 *
 * @Description:     窗口代理类
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/19 13:46
 */
open class UsbWindowCallBackWrapper(private val callBack: Window.Callback) : Window.Callback {

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        return callBack.dispatchKeyEvent(event)
    }

    override fun dispatchKeyShortcutEvent(event: KeyEvent?): Boolean {
        return callBack.dispatchKeyShortcutEvent(event)
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        return callBack.dispatchTouchEvent(event)
    }

    override fun dispatchTrackballEvent(event: MotionEvent?): Boolean {
        return callBack.dispatchTrackballEvent(event)
    }

    override fun dispatchGenericMotionEvent(event: MotionEvent?): Boolean {
        return callBack.dispatchGenericMotionEvent(event)
    }

    override fun dispatchPopulateAccessibilityEvent(event: AccessibilityEvent?): Boolean {
        return callBack.dispatchPopulateAccessibilityEvent(event)
    }

    override fun onCreatePanelView(featureId: Int): View? {
        return callBack.onCreatePanelView(featureId)
    }

    override fun onCreatePanelMenu(featureId: Int, menu: Menu?): Boolean {
        return callBack.onCreatePanelMenu(featureId, menu)
    }

    override fun onPreparePanel(featureId: Int, view: View?, menu: Menu?): Boolean {
        return callBack.onPreparePanel(featureId, view, menu)
    }

    override fun onMenuOpened(featureId: Int, menu: Menu?): Boolean {
        return callBack.onMenuOpened(featureId, menu)
    }

    override fun onMenuItemSelected(featureId: Int, item: MenuItem?): Boolean {
        return callBack.onMenuItemSelected(featureId, item)
    }

    override fun onWindowAttributesChanged(attrs: WindowManager.LayoutParams?) {
        callBack.onWindowAttributesChanged(attrs)
    }

    override fun onContentChanged() {
        callBack.onContentChanged()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        callBack.onWindowFocusChanged(hasFocus)
    }

    override fun onAttachedToWindow() {
        callBack.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        callBack.onDetachedFromWindow()
    }

    override fun onPanelClosed(featureId: Int, menu: Menu?) {
        callBack.onPanelClosed(featureId, menu)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onSearchRequested(searchEvent: SearchEvent?): Boolean {
        return callBack.onSearchRequested(searchEvent)
    }

    override fun onSearchRequested(): Boolean {
        return callBack.onSearchRequested()
    }

    override fun onWindowStartingActionMode(callback: ActionMode.Callback?): ActionMode? {
        return callBack.onWindowStartingActionMode(callback)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onWindowStartingActionMode(
        callback: ActionMode.Callback?,
        type: Int
    ): ActionMode? {
        return callBack.onWindowStartingActionMode(callback, type)
    }

    override fun onActionModeStarted(mode: ActionMode?) {
        callBack.onActionModeStarted(mode)
    }

    override fun onActionModeFinished(mode: ActionMode?) {
        callBack.onActionModeFinished(mode)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onProvideKeyboardShortcuts(
        data: List<KeyboardShortcutGroup?>?, menu: Menu?, deviceId: Int
    ) {
        callBack.onProvideKeyboardShortcuts(data, menu, deviceId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPointerCaptureChanged(hasCapture: Boolean) {
        callBack.onPointerCaptureChanged(hasCapture)
    }
}