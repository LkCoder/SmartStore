package net.luculent.libusb.face

import android.app.Activity
import android.hardware.usb.UsbDevice
import com.blankj.utilcode.util.LogUtils
import com.serenegiant.usb.USBMonitor
import net.luculent.libusb.AbsUsbWindowProxy
import net.luculent.libusb.util.Cfg

/**
 *
 * @Description:     usb监听代理类，activity启动的时候，自动初始化
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/28 15:23
 */
class USBMonitorProxy(activity: Activity) : AbsUsbWindowProxy(activity),
    USBMonitor.OnDeviceConnectListener {

    companion object {
        private const val TAG = "USBMonitorProxy"
    }

    private var mUSBMonitor: USBMonitor? = null

    override fun install() {
        super.install()
        mUSBMonitor = USBMonitor(activity, this, 0)
        mUSBMonitor?.register()
        mUSBMonitor = null
    }

    override fun onDetachedFromWindow() {
        mUSBMonitor?.destroy()
        super.onDetachedFromWindow()
    }

    override fun onAttach(device: UsbDevice?) {
        LogUtils.iTag(TAG, "onAttach: $device")
        device?.let {
            requestPermission(it)
        }
    }

    override fun onDettach(device: UsbDevice?) {
        LogUtils.wTag(TAG, "onDettach: $device")
    }

    override fun onConnect(
        device: UsbDevice?,
        ctrlBlock: USBMonitor.UsbControlBlock?,
        createNew: Boolean
    ) {
        LogUtils.iTag(TAG, "onConnect: $device")
        getUsbInterface().onUSBCameraAttached(USBCamera(activity, device, ctrlBlock))
    }

    override fun onDisconnect(device: UsbDevice?, ctrlBlock: USBMonitor.UsbControlBlock?) {
        LogUtils.wTag(TAG, "onDisconnect: $device")
    }

    override fun onCancel(device: UsbDevice?) {
        LogUtils.wTag(TAG, "onCancel: $device")
    }

    private fun requestPermission(device: UsbDevice) {
        if (device.vendorId == Cfg.OB_DEVICE_VID
            && (Cfg.RGB_LIST.contains(device.productId) || Cfg.IR_LIST.contains(device.productId))
        ) {
            mUSBMonitor?.requestPermission(device)
        }
    }

    private fun getUsbInterface(): IUsbMonitor {
        return activity as IUsbMonitor
    }
}