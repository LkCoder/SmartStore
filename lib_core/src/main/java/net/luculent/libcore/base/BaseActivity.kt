package net.luculent.libcore.base

import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils
import me.jessyan.autosize.AutoSizeCompat
import net.luculent.libcore.toast

/**
 *
 * @Description:     基类activity
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/8 11:17
 */
abstract class BaseActivity : AppCompatActivity(), IMWindow, ISubWindow {

    override fun onCreate(savedInstanceState: Bundle?) {
        initWindow()
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initView()
        initListener()
        initObserver()
        initData()
    }

    override fun initWindow() {
        if (enableImmersionBar()) {
            BarUtils.transparentStatusBar(this)
        }
    }

    override fun getResources(): Resources {
        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources())
        return super.getResources()
    }

    override fun showToast(resId: Int) {
        showToast(resources.getString(resId))
    }

    override fun showToast(tip: CharSequence) {
        toast(tip)
    }

    open fun enableImmersionBar(): Boolean {
        return true
    }
}