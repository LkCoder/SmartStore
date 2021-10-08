package net.luculent.smartstore

import android.content.Intent
import com.blankj.utilcode.util.ActivityUtils
import net.luculent.libcore.base.BaseActivity

/**
 *
 * @Description:    启动页
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/8 17:44
 */
class SplashActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun initData() {
        super.initData()
        ActivityUtils.startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}