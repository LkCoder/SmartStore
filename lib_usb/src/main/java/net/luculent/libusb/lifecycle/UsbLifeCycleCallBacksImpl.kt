package net.luculent.libusb.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import net.luculent.libusb.scan.ICodeScan
import net.luculent.libusb.scan.ScannerProxy

/**
 *
 * @Description:     生命周期管理类
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/19 10:23
 */
class UsbLifeCycleCallBacksImpl : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity is ICodeScan) {
            ScannerProxy().install(activity)
        }
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }
}