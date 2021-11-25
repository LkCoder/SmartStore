package net.luculent.smartstore

import android.Manifest
import android.content.Intent
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.PermissionUtils
import kotlinx.android.synthetic.main.activity_main.*
import net.luculent.face.FaceManager
import net.luculent.libcore.base.BaseActivity
import net.luculent.libcore.base.WindowConfiguration
import net.luculent.smartstore.settings.ServerSettingActivity
import net.luculent.smartstore.verify.VerifyModeActivity

class MainActivity : BaseActivity() {

    private var clickCount = 0

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
        verify_face.setOnClickListener {
            clickCount++
            importUser()
        }
    }

    override fun onPause() {
        super.onPause()
        clickCount = 0
    }

    private fun importUser() {
        if (clickCount < 5) {
            return
        }
        clickCount = 0
        PermissionUtils.permission(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).rationale { activity, shouldRequest -> shouldRequest.again(true) }
            .callback { isAllGranted, granted, deniedForever, denied ->
                if (isAllGranted) {
                    FaceManager.goUserCenter(this)
                } else {
                    showToast("缺少相关权限，请确保已经允许存储权限")
                }
            }.request()
    }

    override fun getWindowConfiguration(): WindowConfiguration {
        return super.getWindowConfiguration().apply {
            fitsSystemWindows = false
        }
    }
}