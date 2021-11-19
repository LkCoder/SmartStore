package net.luculent.face

import net.luculent.face.logger.ILogger

/**
 *
 * @Description:     face-sdk日志
 * @Author:         yanlei.xia
 * @CreateDate:     2021/11/18 18:00
 */
object FaceLogger {

    private const val TAG = "Face-Lib"
    private var logger: ILogger? = null

    fun init(logger: ILogger?) {
        this.logger = logger
    }

    @JvmStatic
    fun d(msg: String?) {
        logger?.d(TAG, msg)
    }

    @JvmStatic
    fun i(msg: String?) {
        logger?.i(TAG, msg)
    }

    @JvmStatic
    fun w(msg: String?) {
        logger?.w(TAG, msg)
    }

    @JvmStatic
    fun e(msg: String?) {
        logger?.e(TAG, msg)
    }
}