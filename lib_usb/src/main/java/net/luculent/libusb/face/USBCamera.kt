package net.luculent.libusb.face

import android.app.Activity
import android.hardware.usb.UsbDevice
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.serenegiant.usb.IFrameCallback
import com.serenegiant.usb.USBMonitor
import com.serenegiant.usbcameracommon.UVCCameraHandler
import com.serenegiant.widget.CameraViewInterface
import java.nio.ByteBuffer

/**
 *
 * @Description:     USB摄像头，包含预览、拍摄、录制
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/28 17:06
 */
class USBCamera(
    private val activity: Activity,
    private val device: UsbDevice?,
    private val ctrlBlock: USBMonitor.UsbControlBlock?
) : IFrameCallback {

    companion object {
        private const val PERFORM_PREVIEW = 3
        private fun getConfiguration() = CameraConfiguration()
    }

    private var mCameraViewInterface: CameraViewInterface? = null
    private var mUVCCameraHandler: UVCCameraHandler? = null
    private var iFrameCallback: IFrameCallback? = null
    private val mHandler by lazy {
        object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    PERFORM_PREVIEW -> {
                        handlePreview()
                    }
                }
            }
        }
    }

    /**
     * called when usb-device connected
     */
    fun init(
        cameraViewInterface: CameraViewInterface,
        cameraConfiguration: CameraConfiguration? = null
    ) {
        if (mUVCCameraHandler?.isOpened == true) {
            mUVCCameraHandler?.close()
        }
        this.mCameraViewInterface = cameraViewInterface
        val previewWidth = cameraConfiguration?.previewWidth ?: getConfiguration().previewWidth
        val previewHeight = cameraConfiguration?.previewHeight ?: getConfiguration().previewHeight
        mCameraViewInterface?.setAspectRatio(previewWidth, previewHeight)
        this.mUVCCameraHandler = UVCCameraHandler.createHandler(
            activity,
            cameraViewInterface,
            previewWidth,
            previewHeight
        )
    }

    /**
     * must call init before
     */
    fun startPreview() {
        mUVCCameraHandler?.apply {
            if (!isOpened) {
                open(ctrlBlock)
            }
        }
        mHandler.removeMessages(PERFORM_PREVIEW)
        mHandler.sendEmptyMessageDelayed(
            PERFORM_PREVIEW,
            100
        )
    }

    fun stopPreview() {
        mUVCCameraHandler?.stopPreview()
    }

    fun setFrameCallback(icb: IFrameCallback) {
        iFrameCallback = icb
    }

    fun takePicture(path: String) {
        mUVCCameraHandler?.captureStill(path)
    }

    fun startRecord() {
        mUVCCameraHandler?.startRecording()
    }

    fun close() {
        mHandler.removeCallbacksAndMessages(null)
        try {
            mUVCCameraHandler?.removeCallbacksAndMessages(null)
            mUVCCameraHandler?.release()
            mUVCCameraHandler = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            mCameraViewInterface?.apply {
                surfaceTexture?.release()
                onPause()
            }
            mCameraViewInterface = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handlePreview() {
        if (mUVCCameraHandler?.isPreviewing == true) {
            return
        }
        mUVCCameraHandler?.apply {
            mCameraViewInterface?.surfaceTexture?.let {
                setShotPicture(this@USBCamera)
                startPreview(it)
            }
        }
    }

    override fun onFrame(frame: ByteBuffer?) {
        iFrameCallback?.onFrame(frame)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is USBCamera) {
            return false
        }
        return device?.deviceId == other.device?.deviceId
    }
}