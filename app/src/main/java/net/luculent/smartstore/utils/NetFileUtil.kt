package net.luculent.smartstore.utils

import android.net.Uri
import net.luculent.libcore.utils.ServerManager
import net.luculent.smartstore.service.UserService

/**
 *
 * @Description:    网络文件处理类
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/25 17:00
 */
object NetFileUtil {

    fun getUrlForBreakpointDownload(fileno: String?): String {
        return getUrlForBreakpointDownload(fileno, "mobileDownloadFileService")
    }

    fun getUrlForBreakpointDownload(fileno: String?, action: String): String {
        var api = ServerManager.getServer()
        try {
            val user = UserService.getUser()
            val server = Uri.parse(api)
            api = server.buildUpon()
                .appendPath("Liems")
                .appendPath("webservice")
                .appendPath(action)
                .appendQueryParameter("userid", user?.userId)
                .appendQueryParameter("assettype", "1")
                .appendQueryParameter("fileno", fileno)
                .appendQueryParameter("type", "1")
                .build().toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return api
    }
}