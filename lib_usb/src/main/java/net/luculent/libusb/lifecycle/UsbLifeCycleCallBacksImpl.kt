package net.luculent.libusb.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.blankj.utilcode.util.LogUtils
import net.luculent.libusb.scan.ICodeScan
import net.luculent.libusb.scan.ScannerProxy

/**
 *
 * @Description:     生命周期管理类
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/19 10:23
 */
class UsbLifeCycleCallBacksImpl : Application.ActivityLifecycleCallbacks {

    companion object {
        private const val TAG = "lib-usb"
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        LogUtils.iTag(TAG, "onActivityCreated= ${activity.localClassName}")
        if (activity is ICodeScan) {
            ScannerProxy(activity).install()
        }
    }

    override fun onActivityStarted(activity: Activity) {
        LogUtils.iTag(TAG, "onActivityStarted= ${activity.localClassName}")
    }

    override fun onActivityResumed(activity: Activity) {
        LogUtils.iTag(TAG, "onActivityResumed= ${activity.localClassName}")
    }

    override fun onActivityPaused(activity: Activity) {
        LogUtils.iTag(TAG, "onActivityPaused= ${activity.localClassName}")
    }

    override fun onActivityStopped(activity: Activity) {
        LogUtils.iTag(TAG, "onActivityStopped= ${activity.localClassName}")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        LogUtils.iTag(TAG, "onActivityDestroyed= ${activity.localClassName}")
    }
}