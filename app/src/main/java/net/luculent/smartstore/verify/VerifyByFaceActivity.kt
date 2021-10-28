package net.luculent.smartstore.verify

import com.serenegiant.widget.UVCCameraTextureView
import net.luculent.libcore.base.BaseActivity
import net.luculent.libcore.base.WindowConfiguration
import net.luculent.libusb.face.IUsbMonitor
import net.luculent.libusb.face.USBCamera
import net.luculent.smartstore.R

/**
 *
 * @Description:     刷脸认证页面
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/28 18:02
 */
class VerifyByFaceActivity : BaseActivity(), IUsbMonitor {

    private var mUsbCamera: USBCamera? = null
    private var facePreview: UVCCameraTextureView? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_verify_face
    }

    override fun initView() {
        super.initView()
        facePreview = findViewById(R.id.face_camera_view)
    }

    override fun onUSBCameraAttached(usbCamera: USBCamera) {
        mUsbCamera = usbCamera
        facePreview?.let {
            usbCamera.init(it)
            usbCamera.startPreview()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mUsbCamera?.close()
    }

    override fun getWindowConfiguration(): WindowConfiguration {
        return super.getWindowConfiguration().apply {
            windowBackgroundColor = R.color.windowBgColor_f4
        }
    }
}