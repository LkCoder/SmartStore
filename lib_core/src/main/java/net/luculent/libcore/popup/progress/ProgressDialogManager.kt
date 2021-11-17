package net.luculent.libcore.popup.progress

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 *
 * @Description:     进度管理器
 * @Author:         yanlei.xia
 * @CreateDate:     2021/11/17 18:06
 */
class ProgressDialogManager private constructor(private val activity: Activity) {

    private var progressDialog: XProgressDialog? = null

    fun show() {
        if (progressDialog == null) {
            progressDialog = XProgressDialog(activity)
        }
        progressDialog?.show()
    }

    fun dismiss() {
        progressDialog?.dismiss()
    }

    companion object {

        @Volatile
        private var lifecycleCallbacks: Application.ActivityLifecycleCallbacks? = null
        private val progressDialogMap = mutableMapOf<String, ProgressDialogManager>()

        fun get(activity: Activity): ProgressDialogManager {
            initApplication(activity.application)
            return register(activity)
        }

        private fun register(activity: Activity): ProgressDialogManager {
            var progressDialogManager = progressDialogMap[getActivityKey(activity)]
            if (progressDialogManager == null) {
                progressDialogManager = ProgressDialogManager(activity)
                progressDialogMap[getActivityKey(activity)] = progressDialogManager
            }
            return progressDialogManager
        }

        private fun unregister(activity: Activity): ProgressDialogManager? {
            return progressDialogMap.remove(getActivityKey(activity))
        }

        private fun getActivityKey(activity: Activity): String {
            return activity.localClassName
        }

        @Synchronized
        private fun initApplication(application: Application) {
            if (lifecycleCallbacks == null) {
                lifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
                    override fun onActivityCreated(
                        activity: Activity,
                        savedInstanceState: Bundle?
                    ) {
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
                        unregister(activity)?.dismiss()
                    }
                }
                application.registerActivityLifecycleCallbacks(lifecycleCallbacks)
            }
        }
    }
}