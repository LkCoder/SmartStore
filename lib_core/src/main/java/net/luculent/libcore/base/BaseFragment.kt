package net.luculent.libcore.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import net.luculent.libcore.toast

/**
 *
 * @Description:     基类fragment
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/8 14:17
 */
abstract class BaseFragment : Fragment(), IMWindow, ISubWindow {

    override fun onCreate(savedInstanceState: Bundle?) {
        initWindow()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        initObserver()
        initData()
    }

    override fun showToast(resId: Int) {
        showToast(resources.getString(resId))
    }

    override fun showToast(tip: CharSequence) {
        context?.toast(tip)
    }
}