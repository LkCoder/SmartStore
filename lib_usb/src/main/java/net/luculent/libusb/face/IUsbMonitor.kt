package net.luculent.libusb.face

import android.hardware.usb.UsbDevice
import net.luculent.libusb.util.Cfg

/**
 *
 * @Description:     usb监听注册器，只要实现了此接口的activity，会自动注册USB监听
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/28 15:18
 */
interface IUsbMonitor {

    /**
     * 是否支持当前usb设备，如果不支持，则不会请求连接
     */
    fun isSupport(device: UsbDevice): Boolean {
        return device.vendorId == Cfg.OB_DEVICE_VID
                && (Cfg.RGB_LIST.contains(device.productId))
    }

    /**
     * 设备连接成功以后的回调，此方法可能会回调多次，如果请求连接了多个摄像头
     */
    fun onUSBCameraConnected(usbCamera: USBCamera)
}