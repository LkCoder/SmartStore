package net.luculent.libcore.utils

import android.net.Uri
import net.luculent.libcore.storage.mkv.Storage

/**
 *
 * @Description:     服务器管理
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/22 17:26
 */
object ServerManager {

    private const val SERVER = "server"

    fun init(url: String) {
        Storage.getInstance().put(SERVER, url)
    }

    fun getServer(): String {
        return Storage.getInstance().get<String>(SERVER) ?: ""
    }

    fun getHost(): String? {
        val url = getServer()
        return try {
            val uri = Uri.parse(url)
            return uri.host
        } catch (e: Exception) {
            e.printStackTrace()
            url
        }
    }

    fun getPort(): String {
        val url = getServer()
        return try {
            val uri = Uri.parse(url)
            return uri.port.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            url
        }
    }
}