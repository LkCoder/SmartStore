package net.luculent.libcore.popup

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import net.luculent.libcore.widget.WindowViewContainer

/**
 * Created by xiayanlei on 2021/10/14
 */
abstract class BaseXDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val windowViewContainer = WindowViewContainer(inflater.context)
        windowViewContainer.setContent(getLayoutId(), Gravity.CENTER)
        return windowViewContainer
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    open fun initView(view: View) {

    }

    abstract fun getLayoutId(): Int
}