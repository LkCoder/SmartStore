package net.luculent.smartstore

import androidx.multidex.MultiDexApplication
import com.blankj.utilcode.util.LogUtils
import net.luculent.libapi.ApiManager
import net.luculent.libcore.utils.ServerManager
import net.luculent.smartstore.api.ApiConfiguration
import net.luculent.smartstore.api.ApiService
import net.luculent.smartstore.starter.BdSDKStarter

/**
 *
 * @Description:     自定义application
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/8 15:25
 */
class StoreApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        initLogger()
        initApi()
        BdSDKStarter.initialize(this)
    }

    private fun initLogger() {
        LogUtils.getConfig().apply {
            globalTag = getString(R.string.app_name)
            saveDays = 7
            isLog2FileSwitch = true
            setFileFilter(LogUtils.I)
        }
    }

    private fun initApi() {
        if (ServerManager.getServer().isEmpty()) {
            ServerManager.init(ApiService.SERVER)
        }
        val configuration = ApiConfiguration()
        ApiManager.init(this, configuration, configuration.getMockConfiguration())
    }
}