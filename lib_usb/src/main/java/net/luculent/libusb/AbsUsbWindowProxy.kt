package net.luculent.libusb

import android.app.Activity
import android.os.Build
import android.view.*
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi

/**
 *
 * @Description:     window窗口代理
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/28 15:25
 */
abstract class AbsUsbWindowProxy(val activity: Activity) : Window.Callback {

    private var mOriginCallBack: Window.Callback? = null

    open fun install() {
        mOriginCallBack = activity.window.callback
        activity.window.callback = this
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        return mOriginCallBack?.dispatchKeyEvent(event) ?: false
    }

    override fun dispatchKeyShortcutEvent(event: KeyEvent?): Boolean {
        return mOriginCallBack?.dispatchKeyShortcutEvent(event) ?: false
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        return mOriginCallBack?.dispatchTouchEvent(event) ?: false
    }

    override fun dispatchTrackballEvent(event: MotionEvent?): Boolean {
        return mOriginCallBack?.dispatchTrackballEvent(event) ?: false
    }

    override fun dispatchGenericMotionEvent(event: MotionEvent?): Boolean {
        return mOriginCallBack?.dispatchGenericMotionEvent(event) ?: false
    }

    override fun dispatchPopulateAccessibilityEvent(event: AccessibilityEvent?): Boolean {
        return mOriginCallBack?.dispatchPopulateAccessibilityEvent(event) ?: false
    }

    override fun onCreatePanelView(featureId: Int): View? {
        return mOriginCallBack?.onCreatePanelView(featureId)
    }

    override fun onCreatePanelMenu(featureId: Int, menu: Menu): Boolean {
        return mOriginCallBack?.onCreatePanelMenu(featureId, menu) ?: false
    }

    override fun onPreparePanel(featureId: Int, view: View?, menu: Menu): Boolean {
        return mOriginCallBack?.onPreparePanel(featureId, view, menu) ?: false
    }

    override fun onMenuOpened(featureId: Int, menu: Menu): Boolean {
        return mOriginCallBack?.onMenuOpened(featureId, menu) ?: false
    }

    override fun onMenuItemSelected(featureId: Int, item: MenuItem): Boolean {
        return mOriginCallBack?.onMenuItemSelected(featureId, item) ?: false
    }

    override fun onWindowAttributesChanged(attrs: WindowManager.LayoutParams?) {
        mOriginCallBack?.onWindowAttributesChanged(attrs)
    }

    override fun onContentChanged() {
        mOriginCallBack?.onContentChanged()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        mOriginCallBack?.onWindowFocusChanged(hasFocus)
    }

    override fun onAttachedToWindow() {
        mOriginCallBack?.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        mOriginCallBack?.onDetachedFromWindow()
    }

    override fun onPanelClosed(featureId: Int, menu: Menu) {
        mOriginCallBack?.onPanelClosed(featureId, menu)
    }

    override fun onSearchRequested(): Boolean {
        return mOriginCallBack?.onSearchRequested() ?: false
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onSearchRequested(searchEvent: SearchEvent?): Boolean {
        return mOriginCallBack?.onSearchRequested(searchEvent) ?: false
    }

    override fun onWindowStartingActionMode(callback: ActionMode.Callback?): ActionMode? {
        return mOriginCallBack?.onWindowStartingActionMode(callback)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onWindowStartingActionMode(
        callback: ActionMode.Callback?,
        type: Int
    ): ActionMode? {
        return mOriginCallBack?.onWindowStartingActionMode(callback, type)
    }

    override fun onActionModeStarted(mode: ActionMode?) {
        mOriginCallBack?.onActionModeStarted(mode)
    }

    override fun onActionModeFinished(mode: ActionMode?) {
        mOriginCallBack?.onActionModeFinished(mode)
    }
}