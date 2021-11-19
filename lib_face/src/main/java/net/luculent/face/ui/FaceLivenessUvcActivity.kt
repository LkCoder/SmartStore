package net.luculent.face.ui

import android.view.Surface
import android.view.View
import com.baidu.idl.face.platform.FaceStatusNewEnum
import com.baidu.idl.face.platform.model.ImageInfo
import com.serenegiant.usb.IFrameCallback
import com.serenegiant.widget.CameraViewInterface
import com.serenegiant.widget.UVCCameraTextureView
import net.luculent.face.FaceCallBack
import net.luculent.face.FaceLogger
import net.luculent.face.FaceManager.registerFaceVerifyCallBack
import net.luculent.face.FaceManager.unregisterFaceVerifyCallBack
import net.luculent.face.FaceManager.verify
import net.luculent.face.api.FaceException
import net.luculent.face.api.response.FaceUser
import net.luculent.face.ui.TimeoutDialog.OnTimeoutDialogClickListener
import net.luculent.libusb.face.IUsbMonitor
import net.luculent.libusb.face.USBCamera
import java.nio.ByteBuffer
import java.util.*
import kotlin.collections.Map.Entry

/**
 *
 * @Description:     uvc人脸认证
 * @Author:         yanlei.xia
 * @CreateDate:     2021/11/19 17:49
 */
class FaceLivenessUvcActivity : FaceLivenessBaseActivity(), IUsbMonitor,
    CameraViewInterface.Callback, IFrameCallback, OnTimeoutDialogClickListener,
    FaceCallBack<FaceUser> {

    private var isSurfaceCreated = false

    private var usbCamera: USBCamera? = null
    private var mFaceView: UVCCameraTextureView? = null
    private var mTimeoutDialog: TimeoutDialog? = null

    override fun initListener() {
        super.initListener()
        registerFaceVerifyCallBack(this)
    }

    override fun getFaceSurface(): View {
        val textView = UVCCameraTextureView(this)
        textView.setCallback(this)
        mFaceView = textView
        return textView
    }

    override fun startPreview() {
        if (isSurfaceCreated) {
            usbCamera?.startPreview()
        }
    }

    override fun stopPreview() {
        usbCamera?.stopPreview()
    }

    override fun onUSBCameraConnected(usbCamera: USBCamera) {
        if (this.usbCamera == null) {
            this.usbCamera = usbCamera.apply {
                setFrameCallback(this@FaceLivenessUvcActivity)
            }
            mFaceView?.let {
                usbCamera.init(it)
            }
        }
    }

    override fun onSurfaceCreated(view: CameraViewInterface?, surface: Surface?) {
        isSurfaceCreated = true
        mFaceView?.rotation = -90f
    }

    override fun onSurfaceChanged(
        view: CameraViewInterface?,
        surface: Surface?,
        width: Int,
        height: Int
    ) {
        surface?.let { startPreview() }
    }

    override fun onSurfaceDestroy(view: CameraViewInterface?, surface: Surface?) {
        isSurfaceCreated = false
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterFaceVerifyCallBack(this)
        usbCamera?.close()
        usbCamera = null
    }

    override fun onFrame(frame: ByteBuffer?) {
        frame?.let {
            it.flip() //重置limit和position的值
            val len = it.limit() - it.position()
            val data = ByteArray(len)
            for (i in 0 until len) {
                data[i] = it.get()
            }
            detectFace(640, 480, 270, data)
        }
    }

    override fun onLivenessCompletion(
        status: FaceStatusNewEnum,
        message: String?,
        base64ImageCropMap: HashMap<String, ImageInfo>?,
        base64ImageSrcMap: HashMap<String, ImageInfo>?,
        currentLivenessCount: Int
    ) {
        super.onLivenessCompletion(
            status,
            message,
            base64ImageCropMap,
            base64ImageSrcMap,
            currentLivenessCount
        )
        if (status == FaceStatusNewEnum.OK && mIsCompletion) {
            // 获取最优图片
            getBestImage(base64ImageSrcMap)
        } else if (status == FaceStatusNewEnum.DetectRemindCodeTimeout) {
            if (mViewBg != null) {
                mViewBg!!.visibility = View.VISIBLE
            }
            showMessageDialog()
        }
    }

    /**
     * 获取最优图片
     *
     * @param imageSrcMap 原图集合
     */
    private fun getBestImage(imageSrcMap: HashMap<String, ImageInfo>?) {
        var faceBase64 = ""
        // 将原图集合中的图片按照质量降序排序，最终选取质量最优的一张原图图片
        if (imageSrcMap != null && imageSrcMap.size > 0) {
            val sourceList: List<Entry<String, ImageInfo>> = ArrayList<Entry<String, ImageInfo>>(
                imageSrcMap.entries
            )
            Collections.sort(
                sourceList
            ) { o1: Entry<String, ImageInfo>, o2: Entry<String, ImageInfo> ->
                val key1 = o1.key.split("_".toRegex()).toTypedArray()
                val score1 = key1[2]
                val key2 = o2.key.split("_".toRegex()).toTypedArray()
                val score2 = key2[2]
                score2.toFloat().compareTo(score1.toFloat())
            }
            faceBase64 = sourceList[0].value.base64
        }
        verify(faceBase64)
    }

    private fun showMessageDialog() {
        mTimeoutDialog = TimeoutDialog(this).apply {
            setDialogListener(this@FaceLivenessUvcActivity)
            setCanceledOnTouchOutside(false)
            setCancelable(false)
        }
        mTimeoutDialog?.show()
        onPause()
    }

    override fun onRecollect() {
        mTimeoutDialog?.dismiss()
        mViewBg?.visibility = View.GONE
        onResume()
    }

    override fun onReturn() {
        mTimeoutDialog?.dismiss()
        finish()
    }

    override fun onError(exception: FaceException) {
        FaceLogger.e("face verify error= $exception")
        finish()
    }

    override fun onSuccess(result: FaceUser) {
        finish()
    }
}