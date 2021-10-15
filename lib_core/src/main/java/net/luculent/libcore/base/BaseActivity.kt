package net.luculent.libcore.base

import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import me.jessyan.autosize.AutoSizeCompat
import net.luculent.libcore.mvvm.ViewModelFactory
import net.luculent.libcore.toast
import net.luculent.libcore.widget.WindowViewContainer

/**
 *
 * @Description:     基类activity
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/8 11:17
 */
abstract class BaseActivity : AppCompatActivity(), IMWindow, ISubWindow {

    private lateinit var containerView: WindowViewContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        ViewModelFactory.inject(this)
        initWindow(getWindowConfiguration())
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initView()
        initListener()
        initObserver()
        initData()
    }

    override fun initWindow(configuration: WindowConfiguration) {
        containerView = WindowViewContainer(this).apply {
            initConfiguration(configuration)
        }
        if (configuration.enableImmersionBar) {
            val statusBarColor = ColorUtils.getColor(configuration.statusBarColor)
            val windowColor = ColorUtils.getColor(configuration.windowBackgroundColor)
            BarUtils.setStatusBarColor(window, statusBarColor, configuration.fitsSystemWindows)
            val isLightMode =
                if (configuration.fitsSystemWindows) {
                    if (configuration.statusBarColor == android.R.color.transparent) {//状态栏是透明的，那么使用的是背景色
                        if (configuration.windowBackgroundColor == android.R.color.transparent) {
                            true
                        } else {
                            ColorUtils.isLightColor(windowColor)
                        }
                    } else {
                        ColorUtils.isLightColor(statusBarColor)
                    }
                } else {
                    ColorUtils.isLightColor(windowColor)
                }
            BarUtils.setStatusBarLightMode(this, configuration.isLightMode and isLightMode)
        }
    }

    override fun setContentView(layoutResID: Int) {
        containerView.setContent(layoutResID)
        super.setContentView(containerView)
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

    open fun getWindowConfiguration(): WindowConfiguration {
        return WindowConfiguration(true)
    }
}