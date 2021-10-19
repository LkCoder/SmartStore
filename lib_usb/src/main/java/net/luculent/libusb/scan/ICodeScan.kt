package net.luculent.libusb.scan

/**
 *
 * @Description:     扫描接口，所有需要处理扫描结果的页面都可以实现此方法
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/19 10:33
 */
interface ICodeScan {
    fun onScanResult(value: String)
}