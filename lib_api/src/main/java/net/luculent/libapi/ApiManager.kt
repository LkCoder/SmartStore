package net.luculent.libapi

import android.content.Context
import net.luculent.libapi.http.HttpConfiguration
import net.luculent.libapi.http.HttpFactory
import net.luculent.libapi.mock.MockConfiguration
import net.luculent.libapi.mock.MockFactory

/**
 *
 * @Description:     api管理器
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/12 15:51
 */
object ApiManager {

    fun init(
        context: Context,
        httpConfiguration: HttpConfiguration,
        mockConfiguration: MockConfiguration? = null
    ) {
        HttpFactory.init(httpConfiguration)
        MockFactory.init(context, mockConfiguration)
    }

    fun reset() {
        HttpFactory.clearCache()
    }
}