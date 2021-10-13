package net.luculent.smartstore

import androidx.multidex.MultiDexApplication
import net.luculent.libapi.ApiManager
import net.luculent.smartstore.api.ApiConfiguration

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
        val configuration = ApiConfiguration()
        ApiManager.init(this, configuration, configuration.getMockConfiguration())
    }
}