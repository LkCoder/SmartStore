package net.luculent.smartstore

import android.content.Intent
import com.blankj.utilcode.util.ActivityUtils
import kotlinx.android.synthetic.main.activity_main.*
import net.luculent.libcore.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initListener() {
        super.initListener()
        start_calculate_btn.setOnClickListener {
            ActivityUtils.startActivity(Intent(this, VerifyModeActivity::class.java))
        }
    }
}