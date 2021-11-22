package net.luculent.libusb.face

import android.app.Activity
import android.hardware.usb.UsbDevice
import com.blankj.utilcode.util.LogUtils
import com.serenegiant.usb.USBMonitor
import net.luculent.libusb.AbsUsbWindowProxy

/**
 * 需要注意，usb的回调是在异步线程，处理ui相关的逻辑，需要切回到主线程
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
    }

    override fun onDetachedFromWindow() {
        mUSBMonitor?.destroy()
        mUSBMonitor = null
        super.onDetachedFromWindow()
    }

    override fun onAttach(device: UsbDevice?) {
        LogUtils.iTag(getTag(), "onAttach: $device")
        activity.runOnUiThread {
            device?.let {
                requestPermission(it)
            }
        }
    }

    override fun onDettach(device: UsbDevice?) {
        LogUtils.wTag(getTag(), "onDettach: $device")
    }

    override fun onConnect(
        device: UsbDevice?,
        ctrlBlock: USBMonitor.UsbControlBlock?,
        createNew: Boolean
    ) {
        LogUtils.iTag(getTag(), "onConnect: $device")
        activity.runOnUiThread {
            getUsbInterface().onUSBCameraConnected(USBCamera(activity, device, ctrlBlock))
        }
    }

    override fun onDisconnect(device: UsbDevice?, ctrlBlock: USBMonitor.UsbControlBlock?) {
        LogUtils.wTag(getTag(), "onDisconnect: $device")
    }

    override fun onCancel(device: UsbDevice?) {
        LogUtils.wTag(getTag(), "onCancel: $device")
    }

    private fun requestPermission(device: UsbDevice) {
        if (getUsbInterface().isSupport(device)) {
            mUSBMonitor?.requestPermission(device)
        }
    }

    private fun getUsbInterface(): IUsbMonitor {
        return activity as IUsbMonitor
    }

    private fun getTag(): String {
        return TAG + activity.localClassName
    }
}