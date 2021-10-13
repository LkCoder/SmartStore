package net.luculent.libcore.mvvm

/**
 *
 * @Description:     viewmodel注解，在activity初始化的时候会自动解析
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/13 14:01
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class BindViewModel()
