package net.luculent.smartstore.verify

import android.view.Surface
import android.view.View
import com.serenegiant.widget.CameraViewInterface
import com.serenegiant.widget.UVCCameraTextureView
import kotlinx.android.synthetic.main.activity_verify_face.*
import net.luculent.libcore.base.BaseActivity
import net.luculent.libcore.base.WindowConfiguration
import net.luculent.libusb.face.IUsbMonitor
import net.luculent.libusb.face.SimpleSurfaceCallback
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
    private var isInitialed = false
    private val surfaceCallback by lazy {
        object : SimpleSurfaceCallback() {
            override fun onSurfaceCreated(view: CameraViewInterface?, surface: Surface?) {
                facePreview?.rotation = -90f
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_verify_face
    }

    override fun initView() {
        super.initView()
        facePreview = findViewById(R.id.face_camera_view)
        facePreview?.setCallback(surfaceCallback)
        initCamera()
    }

    override fun onUSBCameraConnected(usbCamera: USBCamera) {
        mUsbCamera = usbCamera
        initCamera()
    }

    private fun initCamera() {
        facePreview?.let {
            if (!isInitialed) {
                mUsbCamera?.apply {
                    face_outline_view.visibility = View.VISIBLE
                    isInitialed = true
                    init(it)
                    startPreview()
                }
            }
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