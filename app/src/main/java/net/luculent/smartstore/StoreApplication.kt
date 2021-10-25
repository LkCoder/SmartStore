package net.luculent.smartstore

import androidx.multidex.MultiDexApplication
import net.luculent.libapi.ApiManager
import net.luculent.libcore.utils.ServerManager
import net.luculent.smartstore.api.ApiConfiguration
import net.luculent.smartstore.api.ApiService

/**
 *
 * @Description:     自定义application
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/8 15:25
 */
class StoreApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        initApi()
    }

    private fun initApi() {
        if (ServerManager.getServer().isEmpty()) {
            ServerManager.init(ApiService.SERVER)
        }
        val configuration = ApiConfiguration()
        ApiManager.init(this, configuration, configuration.getMockConfiguration())
    }
}