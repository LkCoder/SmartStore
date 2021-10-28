package net.luculent.libusb.face

/**
 *
 * @Description:     usb监听注册器，只要实现了此接口的activity，会自动注册USB监听
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/28 15:18
 */
interface IUsbMonitor {
    fun onUSBCameraAttached(usbCamera: USBCamera)
}