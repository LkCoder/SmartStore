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

    fun d(msg: String?) {
        logger?.d(TAG, msg)
    }

    fun i(msg: String?) {
        logger?.i(TAG, msg)
    }

    fun w(msg: String?) {
        logger?.w(TAG, msg)
    }

    fun e(msg: String?) {
        logger?.e(TAG, msg)
    }
}