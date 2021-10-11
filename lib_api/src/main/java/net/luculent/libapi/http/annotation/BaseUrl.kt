package net.luculent.libapi.http.annotation

/**
 *
 * @Description:     retrofit baseUrl
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/11 10:23
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class BaseUrl(val value: String) {
}
