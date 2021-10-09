package net.luculent.smartstore

import android.content.Intent
import com.blankj.utilcode.util.ActivityUtils
import kotlinx.android.synthetic.main.activity_verify_mode.*
import net.luculent.libcore.base.BaseActivity
import net.luculent.libcore.base.WindowConfiguration
import net.luculent.smartstore.goods.GoodsListActivity

/**
 *
 * @Description:     认证方式选择页面
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/8 16:54
 */
class VerifyModeActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_verify_mode
    }

    override fun initListener() {
        super.initListener()
        store_verify_by_code_lt.setOnClickListener {
            ActivityUtils.startActivity(Intent(this, GoodsListActivity::class.java))
        }
        store_verify_cancel.setOnClickListener {
            finish()
        }
    }

    override fun getWindowConfiguration(): WindowConfiguration {
        return super.getWindowConfiguration().apply {
            windowBackgroundColor = R.color.windowBgColor_f4
        }
    }
}