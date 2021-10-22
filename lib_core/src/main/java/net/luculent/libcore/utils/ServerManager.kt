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

    fun getServerExcludePath(): String {
        val url = getServer()
        return try {
            val uri = Uri.parse(url)
            uri.scheme + "://" + uri.host + ":" + uri.port
        } catch (e: Exception) {
            e.printStackTrace()
            url
        }
    }
}