package net.luculent.face.logger

/**
 *
 * @Description:     日志打印类
 * @Author:         yanlei.xia
 * @CreateDate:     2021/11/18 18:01
 */
interface ILogger {
    fun d(tag: String, msg: String?)
    fun i(tag: String, msg: String?)
    fun w(tag: String, msg: String?)
    fun e(tag: String, msg: String?)
}