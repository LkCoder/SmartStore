package net.luculent.libapi.mock.utils

import android.content.Context
import java.io.BufferedReader
import java.io.Closeable
import java.io.IOException
import java.io.InputStreamReader

/**
 *
 * @Description:     asset工具类
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/12 13:57
 */
object AssetsUtil {

    fun readAsString(context: Context, path: String): String? {
        var bufferedReader: BufferedReader? = null
        return try {
            val buf = StringBuilder()
            bufferedReader = BufferedReader(InputStreamReader(context.assets.open(path), "UTF-8"))
            var str: String?
            while (bufferedReader.readLine().also { str = it } != null) {
                buf.append(str)
            }
            buf.toString()
        } catch (e: IOException) {
            null
        } finally {
            close(bufferedReader)
        }
    }

    private fun close(c: Closeable?) {
        try {
            c?.close()
        } catch (e: Exception) {
        }
    }
}