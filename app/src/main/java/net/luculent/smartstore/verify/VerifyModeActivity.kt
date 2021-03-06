package net.luculent.smartstore.verify

import android.Manifest
import android.content.Intent
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.PermissionUtils
import kotlinx.android.synthetic.main.activity_verify_mode.*
import net.luculent.libcore.base.BaseActivity
import net.luculent.libcore.base.WindowConfiguration
import net.luculent.libcore.mvvm.BindViewModel
import net.luculent.smartstore.R
import net.luculent.smartstore.api.response.UserInfo
import net.luculent.smartstore.dialog.LoginDialog
import net.luculent.smartstore.goods.GoodsListActivity
import net.luculent.smartstore.pick.PickListActivity
import net.luculent.smartstore.pick.PickViewModel

/**
 *
 * @Description:     认证方式选择页面
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/8 16:54
 */
class VerifyModeActivity : BaseActivity() {

    @BindViewModel
    lateinit var loginViewModel: LoginViewModel

    @BindViewModel
    lateinit var pickViewModel: PickViewModel

    override fun getLayoutId(): Int {
        return R.layout.activity_verify_mode
    }

    override fun initListener() {
        super.initListener()
        store_verify_by_face_lt.setOnClickListener {
            doFaceVerify()
        }
        store_verify_by_code_lt.setOnClickListener {
            doLogin()
        }
        store_verify_cancel.setOnClickListener {
            finish()
        }
    }

    override fun initObserver() {
        super.initObserver()
        loginViewModel.faceUserData.observe(this, Observer {
            if (it == null) {
                showToast(R.string.face_verify_failed)
            } else {
                onLoginSuccess(UserInfo(it.user_id, ""))
            }
        })
        pickViewModel.pickListLiveData.observe(this, Observer {
            hideLoading()
            if (it?.rows?.size == 1) {//只有一个，跳转到物资扫描页面
                val pickNo = it.rows[0].pickNo
                GoodsListActivity.start(this, pickNo)
                finish()
            } else {//跳转到领料单列表页面
                ActivityUtils.startActivity(Intent(this, PickListActivity::class.java))
                finish()
            }
        })
        pickViewModel.launchLiveData.observe(this, Observer {
            if (!it) {
                hideLoading()
                showToast(R.string.store_server_exception)
            }
        })
    }

    private fun doFaceVerify() {
        PermissionUtils.permission(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).rationale { activity, shouldRequest -> shouldRequest.again(true) }
            .callback { isAllGranted, granted, deniedForever, denied ->
                if (isAllGranted) {
                    loginViewModel.faceVerify(this)
                } else {
                    showToast("缺少相关权限，请确保已经允许相机和存储权限")
                }
            }.request()
    }

    private fun doLogin() {
        LoginDialog.start(
            this,
            getString(R.string.account_login_title),
            object : LoginDialog.LoginCallBack {
                override fun onLoginStart() {
                    showLoading()
                }

                override fun onLoginResult(user: UserInfo?) {
                    if (user != null) {
                        onLoginSuccess(user)
                    } else {
                        hideLoading()
                    }
                }
            })
    }

    private fun onLoginSuccess(user: UserInfo) {
        showLoading()
        loginViewModel.saveUser(user)
        pickViewModel.getPickList()
    }

    override fun getWindowConfiguration(): WindowConfiguration {
        return super.getWindowConfiguration().apply {
            windowBackgroundColor = R.color.windowBgColor_f4
        }
    }
}