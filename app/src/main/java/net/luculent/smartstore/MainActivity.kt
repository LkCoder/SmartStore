package net.luculent.smartstore

import android.content.Intent
import com.blankj.utilcode.util.ActivityUtils
import kotlinx.android.synthetic.main.activity_main.*
import net.luculent.face.FaceManager
import net.luculent.libcore.base.BaseActivity
import net.luculent.libcore.base.WindowConfiguration
import net.luculent.libusb.face.IUsbMonitor
import net.luculent.libusb.face.USBCamera
import net.luculent.smartstore.settings.ServerSettingActivity
import net.luculent.smartstore.verify.VerifyModeActivity

class MainActivity : BaseActivity(), IUsbMonitor {

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initListener() {
        super.initListener()
        start_calculate_btn.setOnClickListener {
            ActivityUtils.startActivity(Intent(this, VerifyModeActivity::class.java))
        }
        server_info_iv.setOnClickListener {
            ActivityUtils.startActivity(Intent(this, ServerSettingActivity::class.java))
        }
    }

    override fun getWindowConfiguration(): WindowConfiguration {
        return super.getWindowConfiguration().apply {
            fitsSystemWindows = false
        }
    }

    override fun onUSBCameraConnected(usbCamera: USBCamera) {
        FaceManager.setUvcCamera(true)
    }
}